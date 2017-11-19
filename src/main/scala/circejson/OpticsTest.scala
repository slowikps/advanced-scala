package circejson

object OpticsTest {

  import cats.syntax.either._
  import io.circe._, io.circe.parser._

  val json: Json = parse("""{
    "order": {
      "customer": {
        "name": "Custy McCustomer",
        "contactDetails": {
          "address": "1 Fake Street, London, England",
          "phone": "0123-456-789"
        }
      },
      "items": [{
        "id": 123,
        "description": "banana",
        "quantity": 1
      }, {
        "id": 456,
        "description": "apple",
        "quantity": 2
      }],
      "total": 123.45
    }
  }
  """).getOrElse(Json.Null)


  import io.circe.optics.JsonPath._

  val _phoneNum = root.order.customer.contactDetails.phone.string


  val phoneNum: Option[String] = _phoneNum.getOption(json)

  println(phoneNum)
}
