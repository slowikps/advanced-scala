package adventofcode
import org.scalatest._
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class Day11SpacePoliceTest extends FlatSpec with Matchers {
  import Day11SpacePolice._
  "Day 11:  my input " should " work" in {

    paintedPanels(
      Source
        .fromFile("src/test/scala/adventofcode/day11Input")
        .getLines
        .flatMap(_.split(","))
        .map(_.toLong)
        .toList
    , 0).paintedPanels() should be(1771)
  }

  "Day 11:  my input part2 " should " work" in {

    paintedPanels(
      Source
        .fromFile("src/test/scala/adventofcode/day11Input")
        .getLines
        .flatMap(_.split(","))
        .map(_.toLong)
        .toList
      , 1).print() should be(249)
  }

}
