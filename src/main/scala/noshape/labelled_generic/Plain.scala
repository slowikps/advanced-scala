package noshape.labelled_generic

import shapeless.Witness
import shapeless.Witness.Aux

object Plain extends App {

  import shapeless.{HList, ::, HNil}
  import shapeless.labelled.{KeyTag, FieldType}
  import shapeless.syntax.singleton._

  val garfield = ("cat" ->> "Garfield") :: ("orange" ->> true) :: HNil

  println(garfield)

  val myName = "name" ->> "Pawel"
  println(myName) //String with shapeless.labelled.KeyTag[String("name"),String]

  trait Cherries
  import shapeless.labelled.field

  val tmp = field[Cherries](123)

  trait NameTag
  trait SurnameTag
  case class SomeStringsAgain(val name: FieldType[NameTag, String], val surnameTag: FieldType[SurnameTag, String])


  val someStringsAgain = SomeStringsAgain(field[NameTag]("ab"), field[SurnameTag]("cd"))
  println(someStringsAgain)


  def printString(in: String) = println(s"I got: $in")

  def getFieldName[K, V](value: FieldType[K, V])
                        (implicit witness: Witness.Aux[K]): K =
    witness.value

  def getFieldValueString[K, V](value: FieldType[K, V])
                               (implicit witness: Witness.Aux[K]): String =
    s"${witness.value}: $value"

  printString(someStringsAgain.name)

  println(getFieldName(myName))
  println(getFieldValueString(myName))

  var cherries1 = "Cherries" ->> "c"
  var cherries2 = field[Cherries]("c")

//  cherries2 = cherries1 - interesting

  println(cherries1 == cherries2)
  println(getFieldValueString(cherries1))

//  implicitly[Witness.Aux[noshape.labelled_generic.Plain.Cherries]]

//  println(getFieldValueString(cherries2))

}
