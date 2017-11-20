package circejson

import circejson.Basics.{Foo, foo, modfiedJson}
import io.circe.parser.decode

object StrictParsing extends App {

  import cats.data.StateT
  import cats.syntax.either._
  import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
  import cats.syntax.show._, io.circe.jawn.decode

//  def read[A: Decoder](k: String): StateT[Decoder.Result, ACursor, A] =
//    StateT[Decoder.Result, ACursor, A] { c =>
//      val field = c.downField(k)
//      field.as[A].map((field.delete, _))
//    }
//
//  def requireEmpty: StateT[Decoder.Result, ACursor, Unit] =
//    StateT[Decoder.Result, ACursor, Unit] { c =>
//      val fields = c.focus.flatMap(_.asObject).toList.flatMap(_.fields)
//      if (fields.isEmpty) ((c, ())).asRight else
//        (DecodingFailure(s"Leftovers: ${ fields.mkString(", ") }", c.history)).asLeft
//    }

  case class Person(name: String, surname: String, age: Int)

  object PersonCodec {
    import cats.instances.either._
    implicit val decodeUser: Decoder[Person] = new Decoder[Person] {
      def getAndRemoveNew[T](name: String)(implicit d: Decoder[T]): StateT[Either[DecodingFailure, ?], ACursor, T] = {
        StateT { cursor =>
          {
            val field = cursor.downField(name)
            field.as[T].map((field.delete, _))
          }
        }
      }

      def checkExtraFields(): StateT[Either[DecodingFailure, ?], ACursor, Unit] = {
        StateT { c =>
          {
            val fields: Seq[String] = c.focus.flatMap(_.asObject).toList.flatMap(_.fields)
            if (fields.isEmpty) (c, ()).asRight
            else (DecodingFailure(s"Leftovers: ${fields.mkString(", ")}", c.history)).asLeft
          }
        }
      }
      override def apply(c: HCursor) = {
        println(c.fields)
        (for {
          age     <- getAndRemoveNew[Int]("age")
          surname <- getAndRemoveNew[String]("surname")
          name    <- getAndRemoveNew[String]("name")
          _       <- checkExtraFields()
        } yield {
          Person(name, surname, age)
        }).runA(c)
      }
    }

    implicit val encodeUser: Encoder[Person] =
      Encoder.forProduct3("name", "surname", "age")(u => (u.name, u.surname, u.age))
  }

  val somePerson = Person("Jan", "Kowalski", 35)

  val personWithExtras = (somePerson.asJsonObject.add("extraData", Json.fromString("Some extra")))

  println(somePerson.asJson.noSpaces)
  println(personWithExtras.asJson.noSpaces)

  val implicitDecoder = Decoder[Person]
  println(implicitDecoder)
  import PersonCodec._
  println(
    "With extras deserialization: " + decode[Person](personWithExtras.asJson.noSpaces)
  )
  println("The end")
}
