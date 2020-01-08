package adventofcode

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Day13CarePackage {

  def game(input: Seq[Long]): Day13InstructionManager = {
    import IntCode._
    intCode(input.toList, new Day13InstructionManager())
  }

}
class Day13InstructionManager extends InstructionManager {
  private val tiles            = mutable.Set[Tile]()
  private var incompleteOutput = mutable.ArrayBuffer[Long]()
  var score                    = 0L
  var paddle: Tile = null
  override def nextInstruction(): Long = {
    println("The ball: " + getTile(4))
    println("The paddle: " + getTile(3))
    val ball: Tile   = tiles.find(_.id == 4).get

    val res = {
      if (ball.position.x == paddle.position.x) 0
      else if (ball.position.x < paddle.position.x) 1
      else -1
    }
    paddle = Tile(paddle.id, Point(paddle.position.x + res, paddle.position.y))
    res
  }

  override def registerOutput(out: Long): Unit = incompleteOutput match {
    case ArrayBuffer(-1, 0) => score = out
    case ArrayBuffer(x, y) =>
      if(out == 3) paddle = Tile(out.toInt, Point(x.toInt, y.toInt))

      tiles += Tile(out.toInt, Point(x.toInt, y.toInt))
      incompleteOutput = ArrayBuffer()
    case ab => ab += out
  }

  def blockTilesCount(): Int = tiles.count(_.id == 2)

  def getTile(idx: Int): mutable.Set[Tile] = tiles.filter(_.id == idx)
}

case class Tile(id: Int, position: Point)
