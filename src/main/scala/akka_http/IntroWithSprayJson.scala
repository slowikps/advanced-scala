package akka_http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.event.Logging.LogLevel
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.{Route, RouteResult}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.RouteResult.{Complete, Rejected}
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry, LoggingMagnet}
import spray.json.DefaultJsonProtocol._

import scala.io.StdIn
import scala.concurrent.Future

object IntroWithSprayJson {

  // needed to run the route
  implicit val system       = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  var orders: List[Item] = Nil

  // domain model
  final case class Item(name: String, id: Long)
  final case class Order(items: List[Item])

  // formats for unmarshalling and marshalling
  implicit val itemFormat  = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order)

  // (fake) async database query api
  def fetchItem(itemId: Long): Future[Option[Item]] = Future {
    orders.find(o => o.id == itemId)
  }
  def saveOrder(order: Order): Future[Done] = {
    orders = order match {
      case Order(items) => items ::: orders
      case _            => orders
    }
    Future { Done }
  }

  def main(args: Array[String]) {

    def akkaResponseTimeLoggingFunction(
                                         loggingAdapter:   LoggingAdapter,
                                         requestTimestamp: Long,
                                         level:            LogLevel       = Logging.InfoLevel)(req: HttpRequest)(res: RouteResult): Unit = {
      val entry = res match {
        case Complete(resp) =>
          val responseTimestamp: Long = System.nanoTime
          val elapsedTime: Long = (responseTimestamp - requestTimestamp) / 1000000
          val loggingString = s"""Logged Request:${req.method}:${req.uri}:${resp.status}:$elapsedTime"""
          LogEntry(loggingString, level)
        case Rejected(reason) =>
          LogEntry(s"Rejected Reason: ${reason.mkString(",")}", level)
      }
      entry.logTo(loggingAdapter)
    }

    def printResponseTime(log: LoggingAdapter): HttpRequest => RouteResult => Unit = {
      val requestTimestamp = System.nanoTime
      akkaResponseTimeLoggingFunction(log, requestTimestamp)(_)
    }

    val route: Route = DebuggingDirectives.logRequestResult(LoggingMagnet(printResponseTime)) {
      get {
        pathPrefix("item" / LongNumber) { id =>
          // there might be no item for a given id
          val maybeItem: Future[Option[Item]] = fetchItem(id)

          onSuccess(maybeItem) {
            case Some(item) => complete(item)
            case None => complete(StatusCodes.NotFound)
          }
        }
      } ~
        post {
          path("create-order") {
            entity(as[Order]) { order =>
              println(s"The order is: $order")
              val saved: Future[Done] = saveOrder(order)
              onComplete(saved) { _ =>
                complete("order created")
              }
            }
          }
        } ~ complete("sorry don't know what you are looking for ...;/")
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done

  }
}
