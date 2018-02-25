package akka_http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, onSuccess, pathPrefix, _}
import akka.http.scaladsl.server.{Directive, Route}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

object DirectivesTest extends App {

  // needed to run the route
  implicit val system       = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future map/flatmap in the end and future in fetchItem and saveOrder
  implicit val executionContext = system.dispatcher

  val route: Route =
    get {
      pathPrefix("number" / LongNumber) { id =>
        // there might be no item for a given id
        val maybeItem: Future[Tuple2[Int, Int]] = Future((1, 2))

        val onSuccessResult: Directive[Tuple2[Int, Int]] = onSuccess(maybeItem)

        onSuccessResult { (x, y) =>
          complete(s"$x + $y + $id")
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done

}
