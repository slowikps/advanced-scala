package circejson

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._
import noshape.Employee
import shapeless.Generic

object ProductTest extends App {

  implicit val config: Configuration = Configuration.default

  case class ProductTest(str: String, int: Int)

  implicit val testEncoder: Encoder[ProductTest] =
    Encoder.forProduct2("stringInput", "intInput")(u => (u.str, u.int))

  implicit val testDecoder: Decoder[ProductTest] =
    Decoder.forProduct2("stringInput", "intInput")(ProductTest.apply)

  val productClass          = ProductTest("firstString", 23)
  val productJson: Json     = productClass.asJson
  val productString: String = productClass.asJson.toString()

  println(productJson)

  val jsonWithExtraField: JsonObject = productJson.asObject.get.+:(("extraField" -> "extraValue".asJson))

  println(jsonWithExtraField.asJson)

  val decodeResult: Result[ProductTest] = testDecoder.decodeJson(productJson)

  def numberOfFields[T](implicit g: Generic[T]): Int = {
    println(g)

    1
  }

  val decoderWithValidate = testDecoder.validate(
    (cursor: HCursor) => {
      println(cursor.keys)
      numberOfFields[ProductTest]
      true
  }, "too many data")
  val decodeWithExtraFields = decoderWithValidate.decodeJson(jsonWithExtraField.asJson)

  println("decodeResult: " + decodeResult)
  println("decodeWithExtraFields: " + decodeWithExtraFields)
  //  productString.as

  println("The end")
}
