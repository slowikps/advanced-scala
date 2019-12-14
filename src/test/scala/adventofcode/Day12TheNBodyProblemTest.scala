package adventofcode
import org.scalatest._
import org.scalatest.matchers.should.Matchers

class Day12TheNBodyProblemTest extends FlatSpec with Matchers {
  import Day12TheNBodyProblem._
  "Day 12:  test input " should " work" in {

    val test = fromString(
                                    """<x=-1, y=0, z=2>
                                      |<x=2, y=-10, z=-7>
                                      |<x=4, y=-8, z=8>
                                      |<x=3, y=5, z=-1>""".stripMargin)

    test.executeMany(0) should be(
      fromStringWithVelocity(
        """pos=<x=-1, y=  0, z= 2>, vel=<x= 0, y= 0, z= 0>
          |pos=<x= 2, y=-10, z=-7>, vel=<x= 0, y= 0, z= 0>
          |pos=<x= 4, y= -8, z= 8>, vel=<x= 0, y= 0, z= 0>
          |pos=<x= 3, y=  5, z=-1>, vel=<x= 0, y= 0, z= 0>""".stripMargin
      )
    )

    test.executeMany(2) should be(
      fromStringWithVelocity(
        """pos=<x= 5, y=-3, z=-1>, vel=<x= 3, y=-2, z=-2>
          |pos=<x= 1, y=-2, z= 2>, vel=<x=-2, y= 5, z= 6>
          |pos=<x= 1, y=-4, z=-1>, vel=<x= 0, y= 3, z=-6>
          |pos=<x= 1, y=-4, z= 2>, vel=<x=-1, y=-6, z= 2>""".stripMargin
      )
    )

    test.executeMany(10) should be(
      fromStringWithVelocity(
        """pos=<x= 2, y= 1, z=-3>, vel=<x=-3, y=-2, z= 1>
          |pos=<x= 1, y=-8, z= 0>, vel=<x=-1, y= 1, z= 3>
          |pos=<x= 3, y=-6, z= 1>, vel=<x= 3, y= 2, z=-3>
          |pos=<x= 2, y= 0, z= 4>, vel=<x= 1, y=-1, z=-1>""".stripMargin
      )
    )

    test.executeMany(10).totalEnergy() should be(179)
  }

  "Day 12:  my example " should " work" in {
    val test = fromString(
      """<x=9, y=13, z=-8>
        |<x=-3, y=16, z=-17>
        |<x=-4, y=11, z=-10>
        |<x=0, y=-2, z=-2>""".stripMargin)

    test.executeMany(1000).totalEnergy() should be(7758)
  }

  "Day 12: site example part 2" should " work" in {
    val test = fromString(
      """<x=-1, y=0, z=2>
        |<x=2, y=-10, z=-7>
        |<x=4, y=-8, z=8>
        |<x=3, y=5, z=-1>""".stripMargin)

    test.stepsToInitialState() should be(2772)
  }

  "Day 12: site part2 example part 2" should " work" in {
    val test = fromString(
      """<x=-8, y=-10, z=0>
        |<x=5, y=5, z=10>
        |<x=2, y=-7, z=3>
        |<x=9, y=-8, z=-3>""".stripMargin)

    test.stepsToInitialState() should be(4686774924l)
  }

  "Day 12:  my example part 2" should " work" in {
    val test = fromString(
      """<x=9, y=13, z=-8>
        |<x=-3, y=16, z=-17>
        |<x=-4, y=11, z=-10>
        |<x=0, y=-2, z=-2>""".stripMargin)

    test.stepsToInitialState() should be(354540398381256L)
  }
}
