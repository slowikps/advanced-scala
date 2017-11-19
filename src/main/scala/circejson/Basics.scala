package circejson

import circejson.Basics.foo

object Basics extends App {

  import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

  import io.circe.generic.extras._
  implicit val config: Configuration = Configuration.default.withSnakeCaseKeys

  sealed trait Foo
  // defined trait Foo

  case class Bar(xs: List[String]) extends Foo
  // defined class Bar

  case class Qux(i: Int, d: Option[Double]) extends Foo
  // defined class Qux

  val foo: Foo = Qux(13, Some(14.0))
  // foo: Foo = Qux(13,Some(14.0))

  println(
    foo.asJson.noSpaces
  )
  println(
    Qux(13, Some(14.0)).asJson.noSpaces
  )

  val cur       = foo.asJson.hcursor
  val fooAsJson = foo.asJson
  val direcJson = Qux(13, Some(14.0)).asJson
  println(s"$cur $fooAsJson $direcJson")
  // res0: String = {"Qux":{"i":13,"d":14.0}}

  val modfiedJson: JsonObject = (foo.asJsonObject.add("extra", Json.fromString("Some extra")))
  println("modfiedJson.asJson.noSpaces: " + modfiedJson.asJson.noSpaces)
  println(
    decode[Foo](modfiedJson.asJson.noSpaces)
  )

}
