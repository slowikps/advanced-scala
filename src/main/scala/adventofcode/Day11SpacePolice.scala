package adventofcode
import adventofcode.IntCode._

import scala.collection.mutable

sealed trait Action
case object Paint extends Action
case object DirectionUpdate extends Action

object Day11SpacePolice {

  def paintedPanels(input: Seq[Long], startColor: Long): Day11InstructionManger =
    intCode(
      mutable.Map(
        input.zipWithIndex
          .map(in => (in._2.toLong, in._1.toLong)): _*
      ) withDefault (l =>
        if (l > -1) 0
        else throw new IllegalArgumentException("Boom negative memory")),
      new Day11InstructionManger(startColor),
    )

  class Day11InstructionManger(val startColor: Long) extends InstructionManager {
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


}
