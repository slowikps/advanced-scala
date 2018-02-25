package monad_transformers.eff

import cats.data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.future._
import java.time.Instant
import scala.concurrent._, duration._

//From https://scastie.scala-lang.org/wjlow/qhNz9tewSgKoN0mA1OChhw/3
object BasicUsage {

  case class Error(msg: String)

  case class PropertyApiUrl(get: String)

  case class PropertyId(get: Int)

  case class Property(desc: String, id: PropertyId)

  case class UserId(get: Int)

  case class User(name: String, id: UserId, propertyId: PropertyId)

  implicit val ec        = scala.concurrent.ExecutionContext.Implicits.global
  implicit val scheduler = ExecutorServices.schedulerFromGlobalExecutionContext // for Future effect

  type _either[R]    = MemberIn[Either[Error, ?], R]
  type _readerUrl[R] = MemberIn[Reader[PropertyApiUrl, ?], R]

  object UserRepository {

    def get(id: UserId): Future[Either[Error, User]] =
      Future.successful(
        if (id.get > 1000) Right(User("Bob", id, PropertyId(123)))
        else Left(Error(s"Id ${id.get} in invalid range"))
      )

  }

  def getUser[R: _either: _Future](id: UserId): Eff[R, User] =
    for {
      errorOrUser <- fromFuture(UserRepository.get(id))
      user        <- fromEither(errorOrUser)
    } yield user

  def getProperty[R: _either: _readerUrl](id: PropertyId): Eff[R, Property] =
    for {
      propertyApiUrl <- ask[R, PropertyApiUrl]
      property <- if (propertyApiUrl.get == "https://production.property-api.com")
                   right(Property("Big house!", id))
                 else
                   left(Error("Wrong URL!"))
    } yield property

  type _logger[R]    = MemberIn[Writer[String, ?], R]
  type _readClock[R] = MemberIn[Reader[Instant, ?], R]

  def logTime[R: _logger: _readClock](): Eff[R, Unit] =
    for {
      time <- ask[R, Instant]
      _    <- tell(s"The current time is $time")
    } yield ()

  def getPropertyForUserId(id: UserId): Either[Error, Property] = {

    type AppStack = Fx.fx5[Either[Error, ?], Reader[PropertyApiUrl, ?], Writer[String, ?], Reader[Instant, ?], TimedFuture]

    val program: Eff[AppStack, Property] = for {
      user     <- getUser[AppStack](id)
      property <- getProperty[AppStack](user.propertyId)
      _        <- logTime[AppStack]() // Call our new function
    } yield property

    val effFuture: Eff[Fx.fx1[TimedFuture], Either[Error, Property]] = program
      .runReader(PropertyApiUrl("https://production.property-api.com"))
      .runEither
      .runReader(Instant.now())
      .runWriterUnsafe[String] {
        case log => println(log) // print log message to stdout
      }

    val future: Future[Either[Error, Property]] = FutureInterpretation.runAsync(effFuture)

    val result = Await.result(future, Duration("3s"))

    result match {
      case Left(e)  => println(e.msg) // log errors
      case Right(p) => println(s"User ${id.get} owns Property ${p.id.get}")
    }

    result
  }

  def main(args: Array[String]): Unit = getPropertyForUserId(UserId(1200))
}
