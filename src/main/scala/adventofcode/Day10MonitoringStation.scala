package adventofcode

import scala.collection.{MapView, mutable}
import scala.collection.mutable.ArrayBuffer

case class AngleAndDistance(to: Point, distance: Double, angle: Double)
case class Point(x: Int, y: Int) {
  def calculateAngleAndDistance(that: Point): AngleAndDistance = {
    val deltaY        = this.y - that.y
    val deltaX        = that.x - this.x
    val result        = Math.toDegrees(Math.atan2(deltaY, deltaX))
    val angle: Double = if (result < 0) 360d + result else result
    val distance      = Math.sqrt(deltaY * deltaY + deltaX * deltaX)
    AngleAndDistance(that, distance, angle)
  }

  def move(direction: Direction): Point = direction match {
    case Up    => Point(x, y + 1)
    case Down  => Point(x, y - 1)
    case Left  => Point(x - 1, y)
    case Right => Point(x + 1, y)
  }
}
object Day10MonitoringStation {

  def monitoringStationLocation(in: String): Point = asteroidDetails(in).maxBy(_._2.size)._1

  def visibleAsteroidsFromBestLocation(in: String): Int = asteroidDetails(in).maxBy(_._2.size)._2.size

  def asteroidDestroyedAt(in: String, position: Int): Point = {
    val angleToAsteroidsSortedByDistance: MapView[Double, ArrayBuffer[AngleAndDistance]] =
      asteroidDetails(in)
        .maxBy(_._2.size) // Best location
        ._2               //Angle to asteroids visible from best location
        .view
        .mapValues(asteroids => ArrayBuffer(asteroids.sortBy(_.distance): _*))

    val (lessThan90Deg, _90AndMoreDeg) = angleToAsteroidsSortedByDistance.keySet.toList.sorted.span(_ < 90)
    val sorted                         = ArrayBuffer(_90AndMoreDeg.head) ++ lessThan90Deg.reverse ++ _90AndMoreDeg.tail.reverse //WTF!! FIX ME!! -> calculateAngleAndDistance should calculate angle not from vertical but from horizontal line

    def destroyInClockwiseOrder(idx: Int): Point = {
      val key               = sorted(idx % sorted.length)
      val asteroidsForAngle = angleToAsteroidsSortedByDistance(key)
      if (asteroidsForAngle.isEmpty) {
        sorted -= key
        destroyInClockwiseOrder(idx)
      } else if (idx == position - 1) asteroidsForAngle.head.to
      else {
        asteroidsForAngle -= asteroidsForAngle.head
        destroyInClockwiseOrder(idx + 1)
      }
    }

    destroyInClockwiseOrder(0)
  }

  private def asteroidDetails(in: String): Seq[(Point, Map[Double, Seq[AngleAndDistance]])] = {
    val asteroids: Seq[Point] = parseMap(in.toList, 0, 0)
    asteroids.map(p1 => {
      p1 -> (for {
        p2 <- asteroids if p1 != p2
      } yield p1.calculateAngleAndDistance(p2)).groupBy(_.angle)
    })
  }

  private def parseMap(in: List[Char], col: Int, row: Int): List[Point] =
    in match {
      case x :: xs if x == '#'  => Point(col, row) :: parseMap(xs, col + 1, row)
      case x :: xs if x == '\n' => parseMap(xs, 0, row + 1)
      case _ :: xs              => parseMap(xs, col + 1, row)
      case Nil                  => Nil
    }
}
