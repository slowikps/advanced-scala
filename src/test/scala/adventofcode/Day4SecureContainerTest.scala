package adventofcode

import org.scalatest._
import org.scalatest.matchers.should.Matchers

class Day4SecureContainerTest extends FlatSpec with Matchers {
  import Day4SecureContainer._

  "Day 4: Secure Container, for my puzzle input is 128392-643281 " should " equal" in {
    differentPasswordsIn(128392 to 643281)(isCorrectPassword) should be (2050)
  }

  "Day 4: password validation samples " should " work" in {
    isCorrectPassword(111111) should be (true)
    isCorrectPassword(223450) should be (false)
    isCorrectPassword(123789) should be (false)
  }

  "Day 4: password validation version 2 samples " should " work" in {
    isCorrectPasswordV2(112233) should be (true)
    isCorrectPasswordV2(123444) should be (false)
    isCorrectPasswordV2(111122) should be (true)
    isCorrectPasswordV2(111111) should be (false)
    isCorrectPasswordV2(111112) should be (false)
  }

  "Day 4: Secure Container, for my puzzle input and alg ver2 " should " equal" in {
    differentPasswordsIn(128392 to 643281)(isCorrectPasswordV2) should be (1390)
  }

}
