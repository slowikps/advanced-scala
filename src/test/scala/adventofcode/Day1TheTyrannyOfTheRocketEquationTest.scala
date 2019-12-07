package adventofcode

import adventofcode.Day1TheTyrannyOfTheRocketEquation.{requiredFuel, requiredFuelWithFuel}
import org.scalatest._
import org.scalatest.matchers.should.Matchers

class Day1TheTyrannyOfTheRocketEquationTest extends FlatSpec with Matchers {

  "Site samples " should "work" in {
    Day1TheTyrannyOfTheRocketEquation.requiredFuel(12) should be (2)
    Day1TheTyrannyOfTheRocketEquation.requiredFuel(14) should be (2)
    Day1TheTyrannyOfTheRocketEquation.requiredFuel(1969) should be (654)
    Day1TheTyrannyOfTheRocketEquation.requiredFuel(100756) should be (33583)
  }

  "Assignment input " should " add up to 3317100" in {
    val planets = Seq(125517, 140694, 65516, 98562, 75660, 133603, 114499, 81732, 119081, 50911, 96650, 98330, 145164, 64851, 67455,
      108208, 102674, 147581, 112059, 62456, 132006, 88738, 72139, 121074, 103936, 65149, 82081, 90168, 134670, 79142, 83296,
      109983, 60250, 61982, 136326, 52980, 79969, 66851, 77049, 59720, 73494, 115708, 109326, 136399, 72950, 82041, 105467, 112321,
      125019, 79213, 107186, 148340, 112833, 125646, 112509, 52396, 59446, 93967, 73179, 88725, 98256, 143303, 57503, 120314,
      147921, 130856, 95561, 145857, 54976, 100605, 77961, 143120, 84127, 130389, 131848, 109542, 119653, 61660, 124800, 61498,
      149675, 143906, 120361, 68328, 104473, 54279, 119945, 122511, 109410, 135350, 112070, 88822, 149086, 64594, 118788, 102569,
      61721, 89170, 83581, 58722)

    planets.map(requiredFuel).sum should be (3317100)
  }

//  Part two
  "Site samples for part two " should "work" in {
    Day1TheTyrannyOfTheRocketEquation.requiredFuelWithFuel(12) should be (2)
    Day1TheTyrannyOfTheRocketEquation.requiredFuelWithFuel(1969) should be (966)
    Day1TheTyrannyOfTheRocketEquation.requiredFuelWithFuel(100756) should be (50346)
  }

  "Assignment input for part two" should " add up to 3317100" in {
    val planets = Seq(125517, 140694, 65516, 98562, 75660, 133603, 114499, 81732, 119081, 50911, 96650, 98330, 145164, 64851, 67455,
      108208, 102674, 147581, 112059, 62456, 132006, 88738, 72139, 121074, 103936, 65149, 82081, 90168, 134670, 79142, 83296,
      109983, 60250, 61982, 136326, 52980, 79969, 66851, 77049, 59720, 73494, 115708, 109326, 136399, 72950, 82041, 105467, 112321,
      125019, 79213, 107186, 148340, 112833, 125646, 112509, 52396, 59446, 93967, 73179, 88725, 98256, 143303, 57503, 120314,
      147921, 130856, 95561, 145857, 54976, 100605, 77961, 143120, 84127, 130389, 131848, 109542, 119653, 61660, 124800, 61498,
      149675, 143906, 120361, 68328, 104473, 54279, 119945, 122511, 109410, 135350, 112070, 88822, 149086, 64594, 118788, 102569,
      61721, 89170, 83581, 58722)

    planets.map(requiredFuelWithFuel).sum should be (4972784)
  }

}
