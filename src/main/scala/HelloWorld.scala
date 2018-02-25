object HelloWorld {

  def main(args: Array[String]): Unit = {
    val fun = (in: Int) => in + 5

    import cats.instances.function._ // for Functor
    import cats.syntax.functor._     // for map

    println(fun.map(_ + 10)(20))
    println(fun(3))

    val func =
      ((x: Int) => x.toDouble).
        map(x => x + 1).
        map(x => x * 2).
        map(x => x + "!")

    println(
      func(123)
    )
  }
}
