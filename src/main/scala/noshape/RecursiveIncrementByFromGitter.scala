//package noshape
//
//object RecursiveIncrementByFromGitter {
//  object inc {
//    // requires shapeless, cats, kittens
//    import shapeless._
//    import cats._, implicits._
//    import cats.sequence._
//    import cats.data.Reader
//
//    type Incrementable[A] = Inc.Aux[A, Reader[Int, A]]
//
//    implicit class Incrementer[A](a: A) {
//      def incBy(n: Int)(implicit ev: Incrementable[A]): A =
//        ev.apply(a).run(n)
//    }
//
//    trait Inc[A] extends DepFn1[A]
//    trait LowPrio {
//      implicit def others[A]: Inc.Aux[A, Reader[Int, A]] =
//        Inc.instance(_.pure[Reader[Int, ?]])
//    }
//    object Inc extends LowPrio {
//      type Aux[I, O] = Inc[I] { type Out = O }
//
//      def instance[I, O](f: I => O): Inc.Aux[I, O] = new Inc[I] {
//        type Out = O
//        def apply(in: I): O = f(in)
//      }
//
//      implicit def ints: Inc.Aux[Int, Reader[Int, Int]] =
//        instance(i => Reader(_ + i))
//
//      implicit def hnil: Inc.Aux[HNil, HNil] = instance(_ => HNil)
//
//      implicit def hcons[H, T <: HList, R <: HList, O](
//                                                        implicit headInc: Lazy[Inc.Aux[H, O]],
//                                                        tailInc: Inc.Aux[T, R]): Inc.Aux[H :: T, O :: R] =
//        instance(v => headInc.value(v.head) :: tailInc(v.tail))
//
//      implicit def gen[T, L <: HList, O <: HList](
//                                                   implicit gen: Generic.Aux[T, L],
//                                                   inc: Lazy[Inc.Aux[L, O]],
//                                                   ev: Sequencer.Aux[O, Reader[Int, ?], L]): Inc.Aux[T, Reader[Int, T]] =
//        instance(v => inc.value(gen.to(v)).sequence.map(gen.from))
//    }
//  }
//}
