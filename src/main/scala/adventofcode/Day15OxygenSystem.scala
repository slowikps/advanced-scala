package adventofcode

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Day15OxygenSystem {

  def game(input: Seq[Long]): Day15DroidInstructionManager = {
    import IntCode._
    intCode(input.toList, new Day15DroidInstructionManager())
  }

}
case class Transit(target: Point, moves: List[Direction], shortestPath: List[Direction])

class Day15DroidInstructionManager extends InstructionManager {
  private var position                                       = Point(0, 0)
  private var movedToReach                                   = List[Direction]()
  val map: mutable.Map[Point, (Long, List[Direction])]       = mutable.Map()
  val toVisit: mutable.ArrayBuffer[(Point, List[Direction])] = addNeighbours()

  var transit: Option[Transit] = None

  private def addNeighbours(): mutable.ArrayBuffer[(Point, List[Direction])] = {
    ArrayBuffer(
      position.move(Up)    -> (movedToReach :+ Up),
      position.move(Down)  -> (movedToReach :+ Down),
      position.move(Left)  -> (movedToReach :+ Left),
      position.move(Right) -> (movedToReach :+ Right)
    ).filter {
      case (p, _) => !map.contains(p)
    }
  }

  override def nextInstruction(): Long = {
//    Only four movement commands are understood:
//      Up (1), Down (2), Left (3), and Right (4).
    def directionToLong(in: Direction): Long = in match {
      case Up    => 1
      case Down  => 2
      case Left  => 3
      case Right => 4
    }

    val next = transit match {
      case None if toVisit.nonEmpty =>
        val (next, moves)         = toVisit.remove(0)
        val path: List[Direction] = calculatePath(movedToReach, moves)
        transit = Some(Transit(next, path.tail, moves))
        Some(path.head)
      case None => None
      case Some(Transit(target, remainingMoves, shortestPath)) =>
        transit = Some(Transit(target, remainingMoves.tail, shortestPath))
        Some(remainingMoves.head)
    }
    next.map(it => movedToReach = movedToReach :+ it)

    next.map(directionToLong).getOrElse(-1L)
  }

  def calculatePath(oldMoves: List[Direction], moves: List[Direction]): List[Direction] = {
    def goBack(moves: List[Direction]): List[Direction] = moves.map(_.opposite()).reverse

    (oldMoves, moves) match {
      case (old @ ox :: oxs, ne @ nx :: nxs) =>
        if (ox == nx) calculatePath(oxs, nxs)
        else goBack(old) ++ ne
      case (Nil, ne) => ne
      case _ => {
        printMap()
        throw new IllegalStateException("Should not happen")
      }
    }
  }

  override def registerOutput(out: Long): Unit = {
    transit match {
      case Some(Transit(dest, moves, shortestPath)) =>
        val moveDirection = movedToReach.last
        if (moves.isEmpty) {
          out match {
            //    0: The repair droid hit a wall. Its position has not changed.
            //    1: The repair droid has moved one step in the requested direction.
            //    2: The repair droid has moved one step in the requested direction; its new position is the location of the oxygen system.
            case 0 =>
              map += (position.move(moveDirection) -> (0, shortestPath))
              movedToReach = shortestPath.init //Remove step as we didn't move
              transit = None
            case x =>
              position = position.move(moveDirection)
              movedToReach = shortestPath
              if (position != dest) {
                throw new IllegalStateException("We didn't reach what we should")
              }
              map += (position -> (x, shortestPath))
              toVisit ++= addNeighbours()
              if (out == 2) {
                println(s"Found oxygen: $position, shortestPath: ${shortestPath}")
              }
              transit = None

          }
        } else if (out == 0) {
          printMap()
          throw new IllegalStateException("There should be no walls during transit")
        } else {
          position = position.move(moveDirection)
        }

      case None => throw new IllegalStateException("Transit should be present here")
    }

  }

  def timeSpread(): Int = {
    val visited = mutable.Set[Point]()
    def neighbours(of: Point): List[Point] = {
      val res = List(
        of.move(Up),
        of.move(Down),
        of.move(Left),
        of.move(Right)
      ).filter(
        p =>
          !visited.contains(p) &&
            map.get(p).exists(_._1 != 0))

      visited ++= res
      res
    }

    def timeSpread(locations: List[Point]): Int =
      if (locations.isEmpty) 0
      else 1 + timeSpread(locations.flatMap(neighbours))

    val (oxygenSystem, _) = map.find(_._2._1 == 2L).get
    timeSpread(List(oxygenSystem)) - 1
  }

  def printMap(): Unit = {
    val rows = map.groupBy(_._1.y)
    val globalMin = {
      val min = map.keys.map(_.x).min
      if (min < 0) min * -1
      else 0
    }
    val tmp: Seq[Int] = (-globalMin to 30).map(i => (i + globalMin) % 10)
    println("   -" + tmp.mkString(""))
    for {
      row <- rows.keySet.min to rows.keySet.max
    } yield {
      val builder = new mutable.StringBuilder(
        "                                                                                                         ")
      rows
        .getOrElse(row, Map())
        .foreach {
          case (Point(x, _), (0, _)) => builder(x + globalMin) = '#'
          case (Point(x, _), (1, _)) => builder(x + globalMin) = '.'
          case (Point(x, _), (2, _)) => builder(x + globalMin) = 'O'
        }
      if (row == position.y) {
        if (builder(position.x + globalMin) == 'O') builder(position.x + globalMin) = '1'
        else builder(position.x + globalMin) = 'd'
      }
      println(s"   $row|".takeRight(4) + builder.toString())
    }
  }
}
