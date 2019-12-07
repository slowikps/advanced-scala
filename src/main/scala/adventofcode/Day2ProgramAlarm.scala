package adventofcode

import scala.collection.mutable.ArrayBuffer

object Day2ProgramAlarm {

  def intCode(input: Seq[Int]): Seq[Int] = {
    def inner(idx: Int, input: ArrayBuffer[Int]): ArrayBuffer[Int] =
      input(idx) match {
        case 1 =>
          input(input(idx + 3)) = input(input(idx + 1)) + input(input(idx + 2))
          inner(idx + 4, input)
        case 2 =>
          input(input(idx + 3)) = input(input(idx + 1)) * input(input(idx + 2))
          inner(idx + 4, input)
        case 99 => input
        case _  => throw new IllegalStateException("Ops")
      }

    inner(0, ArrayBuffer(input: _*)).toList
  }
}
