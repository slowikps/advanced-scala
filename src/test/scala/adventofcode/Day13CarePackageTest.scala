package adventofcode
import org.scalatest._
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class Day13CarePackageTest extends FlatSpec with Matchers {

  import Day13CarePackage._
  "Day 13:  my example " should " work" in {
    game(
      Source
        .fromFile("src/test/scala/adventofcode/day13Input")
        .getLines
        .flatMap(_.split(","))
        .map(_.toLong)
        .toList
    ).blockTilesCount() should be(296)
  }

  "Day 13:  my example part two " should " work" in {
    val ins = Source
      .fromFile("src/test/scala/adventofcode/day13Input")
      .getLines
      .flatMap(_.split(","))
      .map(_.toLong)
      .toList
    game(
      2 :: ins.tail
    ).score should be(296)
  }
}
