package adventofcode

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Day9SensorBoost {

  def intCode(input: Seq[Int], inputValue: Int): Int =
    longCode(input.map(_.toLong), List(inputValue.toLong)).last.toInt


  def longCode(input: Seq[Long], inputValues: List[Long]): Seq[Long] =
    intCode(0
      , mutable.Map(
        input
          .zipWithIndex
          .map(in => (in._2.toLong, in._1.toLong)): _*
        ) withDefault(l =>
          if(l > -1) 0
          else throw new IllegalArgumentException("Boom negative memory")
        )
      , inputValues.to(ArrayBuffer)
      , 0)

  private def intCode(idx: Long, input: mutable.Map[Long, Long], inputSignals: ArrayBuffer[Long], relativeBase: Long): List[Long] = {
//    @tailrec
    def inner(idx: Long): List[Long] = {
      val pm: ParameterMode = ParameterMode.fromLong(input(idx).toInt)
      def getParameter(offset: Long): Long = pm.mode((offset - 1).toInt) match {
        case PositionMode  => input(input(idx + offset))
        case ImmediateMode => input(idx + offset)
        case RelativeMode  => input(input(idx + offset) + relativeBase)
      }
      def writeParameter(offset: Long, value: Long): Unit = pm.mode((offset - 1).toInt) match {
        case PositionMode  => input(input(idx + offset)) = value
        case RelativeMode  => input(input(idx + offset) + relativeBase) = value
      }
      pm.opCode match {
        case 1 =>
          writeParameter(3, getParameter(1) + getParameter(2))
          inner(idx + 4)
        case 2 =>
          writeParameter(3, getParameter(1) * getParameter(2))
          inner(idx + 4)
        case 3 =>
          writeParameter(1, inputSignals.remove(0))
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
          if (getParameter(1) < getParameter(2)) writeParameter(3, 1)
          else writeParameter(3, 0)
          inner(idx + 4)
        case 8 =>
          if (getParameter(1) == getParameter(2))  writeParameter(3, 1)
          else writeParameter(3, 0)
          inner(idx + 4)
        case 9 =>
          val baseAdjustment = getParameter(1)
          println(s"adjustment: ${relativeBase + baseAdjustment}")
          intCode(idx + 2, input, inputSignals, relativeBase + baseAdjustment)
        case 99 => Nil
        case _  => throw new IllegalStateException("Ops")
      }
    }
    inner(idx)
  }
}
