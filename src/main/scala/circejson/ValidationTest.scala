package circejson


import io.circe.HCursor

object ValidationTest extends App {


  case class ProductTest(str: String, int: Int)

  import io.circe.generic.semiauto._
  import io.circe.parser.decode

  def validationMethod(in: HCursor): Boolean = {
    println("Kaboom")
//    println(in.)
    true
  }

  implicit val decoderWithValidation = deriveDecoder[ProductTest].validate(validationMethod, "some message")


  println(
    decode[ProductTest]("""{ "str": "Str", "int": 23, "c": false, "d": null }""")
  )

}
