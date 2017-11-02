package noshape

case class Employee(name: String, number: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

object Intro extends App {

  import shapeless._

  val genericEmployee = Generic[Employee].to(Employee("Dave", 123, false))
  println(genericEmployee)


  val genericIceCream = Generic[IceCream].to(IceCream("Sundae", 1, false))
  println(genericIceCream)

  def genericFun(gen: String :: Int :: Boolean :: HNil): List[String] = List[String](gen(0), gen(1).toString, gen(2).toString)

  println(
    genericFun(genericIceCream)
  )

}
