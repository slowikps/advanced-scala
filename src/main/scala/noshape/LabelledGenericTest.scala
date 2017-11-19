package noshape

import shapeless.LabelledGeneric.Aux

object LabelledGenericTest extends App {

  case class DataCC(name: String, surname: String, age: Int, isNice: Boolean)
  case class DataCCPl(imie: String, surname: String, age: Int, isNice: Boolean)

  import shapeless._

  val labelledGeneric = LabelledGeneric[DataCC]
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
  println("The end")

}
