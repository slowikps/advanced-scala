package noshape


import shapeless.Generic
import shapeless.Generic.Aux
import shapeless.{Coproduct, :+:, CNil, Inl, Inr}
sealed trait Shape
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape

object CoproductSample extends App {

  val gen: shapeless.Generic[noshape.Shape]{type Repr = noshape.Circle :+: noshape.Rectangle :+: shapeless.CNil} = Generic[Shape]
  //Niezły typ!


}
