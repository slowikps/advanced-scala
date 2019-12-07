package adventofcode

object Day1TheTyrannyOfTheRocketEquation extends App {

  def requiredFuel(mass: Int): Int = mass / 3 - 2

  def requiredFuelWithFuel(mass: Int): Int = {
    def inner(mass: Int, next: Int): Int = {
      if(next >= 0) inner(mass + next, requiredFuel(next))
      else mass
    }

    val reqFuel = requiredFuel(mass)
    inner(reqFuel, requiredFuel(reqFuel))
  }

}
