package noshape.labelled_generic

import shapeless.syntax.SingletonOps

object Types extends App {

  import shapeless.syntax.singleton._

  //For some reason narrow stuff is failing the build if outside the function
  def someMethod() = {
    var x = 42.narrow //Get singleton type - instead of nearest non singleton type

    // x = 10 type mismatch;
//      [error]  found   : Int(10)
//      [error]  required: Int(42)
//      [error]     x = 10

    println("Hop hop:")
    println(s"${x + 10}")

//    val theAnswer: 42 = 42 apparently this should be possible is scala 2.12.1 but it is not?


  }


  someMethod()

//  println(x + 3)

}
