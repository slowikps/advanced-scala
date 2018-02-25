package monad_transformers

case class User(name: String, nickname: String, age: Int)
object Me   extends User("Pawel", "slowikps", 33)
object John extends User("John", "johnnone", 45)

case class ValidationError(msg: String)

object UserDao {
  import cats.syntax.option._
  import cats.syntax.eq._        //To get ===
  import cats.instances.string._ //To get Eq[String]

  def getUserBy(name: String): Option[User] =
    if (name === Me.name) Me.some
    else if (name === John.name) John.some
    else None
}

object UserValidator {
  import cats.syntax.either._
  import cats.syntax.eq._        //To get ===
  import cats.instances.string._ //To get Eq[String]

  def isMe(in: User): Either[ValidationError, User] =
    if (in.name === Me.name) Me.asRight
    else ValidationError("It's not me!").asLeft
}
