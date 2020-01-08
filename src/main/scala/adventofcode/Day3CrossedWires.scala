package adventofcode

import scala.collection.immutable

object Day3CrossedWires {

  def smallestNumberOfSteps(w1: String, w2: String): Int = {
    val moves1 =
      executeMoves(parse(w1)).zipWithIndex
        .groupBy(_._1)
        .mapValues(_.minBy(_._2))
    val moves2 =
      executeMoves(parse(w2)).zipWithIndex
        .groupBy(_._1)
        .mapValues(_.minBy(_._2))

    (moves1.keySet intersect moves2.keySet).foldLeft(Int.MaxValue) {
      case (min, pair @ (x, y)) =>
        Math.min(
          moves1(pair)._2 + moves2(pair)._2,
          min
        )
    } + 2
  }
  def manhattanDistance(w1: String, w2: String): Int = {
    val wire1 = parse(w1)
    val wire2 = parse(w2)

    (executeMoves(wire1).toSet intersect executeMoves(wire2).toSet)
      .map(distanceFromStart)
      .min
  }

  private def parse(in: String): List[Move] =
    in.split(",").map(Move.fromString).toList

  private def executeMoves(moves: List[Move]): List[(Int, Int)] = {
    def internal(pos: (Int, Int), moves: List[Move]): List[(Int, Int)] =
      moves match {
        case x :: xs =>
          val (newPos, points) = executeMove(pos, x)
          points ++ internal(newPos, xs)
        case Nil => List()
      }

    internal((0, 0), moves)
  }

  private def executeMove(pos: (Int, Int), move: Move): ((Int, Int), List[(Int, Int)]) = {
    val points: immutable.Seq[(Int, Int)] = (pos, move) match {
      case ((x, y), Move(steps, Up))    => (1 to steps).map(it => (x, y + it))
      case ((x, y), Move(steps, Down))  => (1 to steps).map(it => (x, y - it))
      case ((x, y), Move(steps, Left))  => (1 to steps).map(it => (x - it, y))
      case ((x, y), Move(steps, Right)) => (1 to steps).map(it => (x + it, y))
    }
    (points.last, points.toList)
  }

  private def distanceFromStart(point: (Int, Int)): Int =
    Math.abs(point._1) + Math.abs(point._2)

}

object Move {
  def fromString(in: String): Move = {
    val direction = in.head match {
      case 'U' => Up
      case 'D' => Down
      case 'L' => Left
      case 'R' => Right
    }
    Move(in.tail.toInt, direction)
  }
}
case class Move(steps: Int, direction: Direction)

sealed trait Direction {
  def opposite(): Direction = this match {
    case Up    => Down
    case Down  => Up
    case Left  => Right
    case Right => Left
  }

}

case object Up    extends Direction
case object Down  extends Direction
case object Left  extends Direction
case object Right extends Direction
