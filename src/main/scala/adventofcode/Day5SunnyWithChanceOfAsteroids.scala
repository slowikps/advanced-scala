package adventofcode

import scala.collection.mutable.ArrayBuffer

object Day5SunnyWithChanceOfAsteroids {

  def intCode(input: Seq[Int], inputValue: Int): Int =
    intCode(0, ArrayBuffer(input: _*), (0 to 1000).map(_ => inputValue).to(ArrayBuffer)).last

  def intCode(input: Seq[Int], inputValues: List[Int]): Int = intCode(0, ArrayBuffer(input: _*), inputValues.to(ArrayBuffer)).last

  def intCode(idx: Int, input: ArrayBuffer[Int], inputSignals: ArrayBuffer[Int]): Seq[Int] = {
    def inner(idx: Int): List[Int] = {
      val pm: ParameterMode = ParameterMode.fromInt(input(idx))
      def getParameter(offset: Int): Int = pm.mode(offset - 1) match {
        case PositionMode  => input(input(idx + offset))
        case ImmediateMode => input(idx + offset)
      }
      pm.opCode match {
        case 1 =>
          input(input(idx + 3)) = getParameter(1) + getParameter(2)
          inner(idx + 4)
        case 2 =>
          input(input(idx + 3)) = getParameter(1) * getParameter(2)
          inner(idx + 4)
        case 3 =>
          input(input(idx + 1)) = inputSignals.remove(0)
          inner(idx + 2)
        case 4 =>
          getParameter(1) :: inner(idx + 2)
        case 5 =>
          if (getParameter(1) != 0) inner(getParameter(2))
          else inner(idx + 3)
        case 6 =>
          if (getParameter(1) == 0) inner(getParameter(2))
          else inner(idx + 3)
        case 7 =>
          if (getParameter(1) < getParameter(2)) input(input(idx + 3)) = 1
          else input(input(idx + 3)) = 0
          inner(idx + 4)
        case 8 =>
          if (getParameter(1) == getParameter(2)) input(input(idx + 3)) = 1
          else input(input(idx + 3)) = 0
          inner(idx + 4)
        case 99 => Nil
        case _  => throw new IllegalStateException("Ops")
      }
    }
    inner(idx)
  }
}

case class ParameterMode(opCode: Int, private val modes: List[Mode]) {
  def mode(offset: Int): Mode = modes.applyOrElse(offset, (_: Int) => PositionMode)
    //modes.applyOrElse(offset, (_: Int) => PositionMode)
}
object ParameterMode {
  def fromInt(in: Int): ParameterMode = fromLong(in.toLong)
  def fromLong(in: Long): ParameterMode = {
    val opCode = (in % 100).toInt
    val modes = (in / 100).toString
      .map {
        case '0' => PositionMode
        case '1' => ImmediateMode
        case '2' => RelativeMode
      }
      .toList
      .reverse
    ParameterMode(opCode, modes)
  }
}

sealed trait Mode
case object PositionMode  extends Mode
case object ImmediateMode extends Mode
case object RelativeMode  extends Mode
