package adventofcode
import adventofcode.Day9SensorBoost.intCode

import scala.collection.mutable

sealed trait Action
case object Paint extends Action
case object DirectionUpdate extends Action

object Day11SpacePolice {

  def paintedPanels(input: Seq[Long], startColor: Long): InstructionManger = {
    val instructionManger = new InstructionManger(startColor)
    intCode(
      0,
      mutable.Map(
        input.zipWithIndex
          .map(in => (in._2.toLong, in._1.toLong)): _*
      ) withDefault (l =>
        if (l > -1) 0
        else throw new IllegalArgumentException("Boom negative memory")),
      instructionManger,
      0
    )
    instructionManger
  }

  class InstructionManger(val startColor: Long) {
    private val visitedPositions     = mutable.Map[Point, Long]()
    private var currentLocation      = Point(0, 0)
    private var direction: Direction = Up
    private var action: Action = Paint

    def nextInstruction(): Long =
      if(visitedPositions.isEmpty) startColor
      else visitedPositions.get(currentLocation) match {
        case Some(1) => 1
        case _       => 0
      }

    def registerOutput(out: Long): Unit =
      action match {
        case Paint =>
          visitedPositions += (currentLocation -> out)
//          println(s"location: $currentLocation, painted to: $out")
          action = DirectionUpdate
        case DirectionUpdate => {
          val oldDirection = direction
          val oldLocation = currentLocation
          direction = (direction, out == 0) match {
            case (Up, true) => Left
            case (Up, false) => Right

            case (Down, true) => Right
            case (Down, false) => Left

            case (Left, true) => Down
            case (Left, false) => Up

            case (Right, true) => Up
            case (Right, false) => Down
          }
          direction match {
            case Up => currentLocation = Point(currentLocation.x - 1, currentLocation.y)
            case Down => currentLocation = Point(currentLocation.x + 1, currentLocation.y)
            case Left => currentLocation = Point(currentLocation.x, currentLocation.y - 1)
            case Right => currentLocation = Point(currentLocation.x, currentLocation.y + 1)
          }

//          println(s"from: $oldLocation, to: $currentLocation, from dir: $oldDirection, to dir: $direction, angle: $out")
          action = Paint
        }
      }

    def paintedPanels(): Int = visitedPositions.size

    def print(): Int = {
      val columns: Map[Int, mutable.Map[Point, Long]] = visitedPositions.groupBy(_._1.x)
      for {
        column <- columns.keySet.min to columns.keySet.max
      } yield {
        val builder = new mutable.StringBuilder("                                                                                                         ")
        columns.getOrElse(column, Map()).filter(_._2 == 1)
            .foreach {
              case (Point(_, y), _) => builder(y) = 0
            }

        println(builder.toString())
      }
      paintedPanels()
    }
  }

  //Another copy/paste
  private def intCode(idx: Long,
                      input: mutable.Map[Long, Long],
                      instructionManager: InstructionManger,
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
