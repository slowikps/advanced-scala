package noshape.genericcsv

import shapeless.{::, Generic, HList, HNil}

trait CsvEncoder[A] {
  def encode(value: A): List[String]
}

object CsvEncoder {
  // "Summoner" method
  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] =
    enc

  // "Constructor" method
  def instance[A](func: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      def encode(value: A): List[String] =
        func(value)
    }

  // Globally visible type class instances
}

object GenericCsv extends App {

  def createEncoder[A](func: A => List[String]): CsvEncoder[A] = (value: A) => func(value)
  def createEncoder2[A](func: A => List[String]): A => List[String] = (value: A) => func(value)
  def runnable(fun: => Unit): Runnable = () => fun

  implicit val stringEncoder: CsvEncoder[String] =
    CsvEncoder.instance(str => List(str))

  implicit val intEncoder: CsvEncoder[Int] =
    createEncoder(num => List(num.toString))

  implicit val booleanEncoder: CsvEncoder[Boolean] =
    createEncoder(bool => List(if (bool) "yes" else "no"))

  implicit val hnilEncoder: CsvEncoder[HNil] =
    createEncoder(hnil => Nil)

  implicit def hlistEncoder[H, T <: HList](implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] =
    createEncoder {
      case h :: t =>
        hEncoder.encode(h) ++ tEncoder.encode(t)
    }

  val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = implicitly

  println(
    reprEncoder.encode("abc" :: 123 :: true :: HNil)
  )

  case class Person(name: String, surname: String, age: Int)
  case class PersonNotCompiling(name: String, surname: String, age: Double)

  val john = Person("John", "NotKnown", 25)
  implicit def genericEncoder[A, R](
      implicit
      gen: Generic[A] { type Repr = R }, //Generic.Aux[A, R],
      enc: CsvEncoder[R]
  ): CsvEncoder[A] = createEncoder(a => enc.encode(gen.to(a)))

  println(
    "John: " + CsvEncoder[Person].encode(john)
  )

//  implicit val doubleEncoder: CsvEncoder[Double] =
//    createEncoder(x => List(s"$x"))

  val johnDouble = PersonNotCompiling("John", "NotKnown", 25)
  println(
    //"John: " + CsvEncoder[PersonNotCompiling].encode(johnDouble) // Need a double CsvEncoder
  )

}
