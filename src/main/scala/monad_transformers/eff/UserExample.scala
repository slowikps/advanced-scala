package monad_transformers.eff

import monad_transformers.eff.BasicUsage.UserRepository
import monad_transformers.{User, UserDao, UserValidator, ValidationError}
import org.atnos.eff.{Eff, EitherEffect, Fx, OptionEffect}
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
object UserExample extends App {

  type Stack = Fx.fx2[Option, Either[ValidationError, ?]]

  def getUserBy(name: String): Eff[Stack, User] =
    OptionEffect.fromOption(UserDao.getUserBy(name))

  def getUserBySend(name: String): Eff[Stack, User] =
    Eff.send(UserDao.getUserBy(name))

  def isMe(in: User): Eff[Stack, User] =
    EitherEffect.fromEither(UserValidator.isMe(in))

  def validProgram: Eff[Stack, User] =
    for {
      me           <- getUserBy("Pawel")
      isValid      <- isMe(me)
      isValidAgain <- isMe(isValid)
    } yield (isValidAgain)

  def inValidProgram: Eff[Stack, User] =
    for {
      me           <- getUserBy("John")
      isValid      <- isMe(me)
      isValidAgain <- isMe(isValid)
    } yield (isValidAgain)

  validProgram.runEither.runOption.run match {
    case Some(Right(x)) => println(s"The result is: $x")
    case x              => println(s"Invalid result: $x")
  }

  inValidProgram.runEither.runOption.run match {
    case Some(Right(x)) => println(s"The result is: $x")
    case x              => println(s"Invalid result: $x")
  }

  println(
    validProgram.runOption.runEither.run
  )
  println("This is the end")

}
