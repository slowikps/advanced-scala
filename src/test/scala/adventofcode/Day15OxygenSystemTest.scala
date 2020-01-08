package adventofcode

import org.scalatest._
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class Day15OxygenSystemTest extends FlatSpec with Matchers {

  "Day 15:  my example " should " work" in {
    val result = Day15OxygenSystem.game(
      Source
        .fromFile("src/test/scala/adventofcode/day15Input")
        .getLines
        .flatMap(_.split(","))
        .map(_.toLong)
        .toList
    )
    result.printMap()
    result.map.values.find(_._1 == 2).get._2.length should be(246)
    result.timeSpread() should be(376)
  }

}
