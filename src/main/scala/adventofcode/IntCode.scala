package adventofcode

import scala.collection.mutable

trait InstructionManager {
  def nextInstruction(): Long

  def registerOutput(out: Long): Unit
}

object IntCode {

  def intCode[T <: InstructionManager](input: List[Long], instructionManger: T): T = {
    intCode(
      0,
      mutable.Map(
        input.zipWithIndex
          .map(in => (in._2.toLong, in._1.toLong)): _*
      ),
      instructionManger,
      0
    )
    instructionManger
  }

  def intCode[T <: InstructionManager](input: mutable.Map[Long, Long], instructionManger: T): T = {
    intCode(
      0,
      input,
      instructionManger,
      0
    )
    instructionManger
  }

  private def intCode(idx: Long,
                      input: mutable.Map[Long, Long],
                      instructionManager: InstructionManager,
                      relativeBase: Long): Unit = {
    def inner(idx: Long): Unit = {
      val pm: ParameterMode = ParameterMode.fromLong(input(idx).toInt)
      def getParameter(offset: Long): Long = pm.mode((offset - 1).toInt) match {
        case PositionMode  => input(input(idx + offset))
        case ImmediateMode => input(idx + offset)
        case RelativeMode  => input(input(idx + offset) + relativeBase)
      }
      def writeParameter(offset: Long, value: Long): Unit = pm.mode((offset - 1).toInt) match {
        case PositionMode => input(input(idx + offset)) = value
        case RelativeMode => input(input(idx + offset) + relativeBase) = value
      }
      pm.opCode match {
        case 1 =>
          writeParameter(3, getParameter(1) + getParameter(2))
          inner(idx + 4)
        case 2 =>
          writeParameter(3, getParameter(1) * getParameter(2))
          inner(idx + 4)
        case 3 =>
          writeParameter(1, instructionManager.nextInstruction())
          inner(idx + 2)
        case 4 =>
          instructionManager.registerOutput(getParameter(1))
          inner(idx + 2)
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
          if (getParameter(1) == getParameter(2)) writeParameter(3, 1)
          else writeParameter(3, 0)
          inner(idx + 4)
        case 9 =>
          val baseAdjustment = getParameter(1)
          intCode(idx + 2, input, instructionManager, relativeBase + baseAdjustment)
        case 99 => Nil
        case _  => throw new IllegalStateException("Ops")
      }
    }
    inner(idx)
  }
}
