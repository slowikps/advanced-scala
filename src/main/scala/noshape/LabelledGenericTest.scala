package noshape

import noshape.Magic.generic
import shapeless.LabelledGeneric.Aux
import shapeless.ops.hlist.Last
import shapeless.ops.record.{Fields, Keys, Values}

object LabelledGenericTest extends App {

  case class DataCC(name: String, surname: String, age: Int, isNice: Boolean)
  case class DataCCPl(imie: String, surname: String, age: Int, isNice: Boolean)

  import shapeless._

  val labelledGeneric   = LabelledGeneric[DataCC]
  val labelledGenericPl = LabelledGeneric[DataCCPl]
  //shapeless.LabelledGeneric[DataCC]{type Repr = String with shapeless.labelled.KeyTag[Symbol with shapeless.tag.Tagged[String("name")],String] :: String with shapeless.labelled.KeyTag[Symbol with shapeless.tag.Tagged[String("surname")],String] :: Int with shapeless.labelled.KeyTag[Symbol with shapeless.tag.Tagged[String("age")],Int] :: Boolean with shapeless.labelled.KeyTag[Symbol with shapeless.tag.Tagged[String("isNice")],Boolean] :: shapeless.HNil}

  import shapeless.record._

  val me = labelledGeneric.to(DataCC("pawel", "s", 31, true))


  println(
    me.updateWith('name)("updated " + _)
  )

  println(
    labelledGenericPl.from(me.renameField('name, 'imie))
  )
  println(
    "me.get('name): " + me.get('name)
  )

  println(
    "me.keys: " + me.keys
  )

  val toMapRes: Map[Symbol, Any] = me.toMap


  toMapRes.foreach {
    case (a, _: Int) => println(a.name + " value of type int")
    case (a, _: Boolean) => println(a.name + " value of type Boolean")
    case (a, _) => println(a.name + " value of type Unknown")
  }
  println(
    "me.toMap: " + me.toMap
  )
  println(toMapRes)


  println(s"record: $record")
  println("The end")

  def lastField[A, Repr <: HList](input: A)(
    implicit
    gen: LabelledGeneric.Aux[A, Repr],
    last: Last[Repr],
    keys: Keys[Repr],
    values: Values[Repr],
    fields: Fields[Repr],
  ) = {
    println("keys.apply: " + keys.apply)
//    println("values.apply: " + values.apply)
    println(fields.apply(gen.to(input)))


    keys.apply()
  }


  val keys = implicitly[Keys[labelledGeneric.Repr]].apply
  println("keys: " + keys)
  println("lastField: " + lastField(DataCC("pawel", "s", 31, true)))

  print("labelledGeneric: " + labelledGeneric.toString)
}
