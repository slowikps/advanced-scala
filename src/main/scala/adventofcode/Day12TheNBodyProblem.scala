package adventofcode

case class Day12TheNBodyProblem(moons: List[Moon]) {
  def totalEnergy(): Int = moons.map(_.totalEnergy()).sum

  private def check(xs: List[Int], positions: List[Int], velocity: List[Int]) =
    xs == positions && velocity.forall(_ == 0)

  def stepsToInitialState(): Long = {
    var next                = step()
    var steps: Long         = 1
    var xFreq: Option[Long] = None
    var yFreq: Option[Long] = None
    var zFreq: Option[Long] = None
    var xs                  = moons.map(_.position.x)
    var ys                  = moons.map(_.position.y)
    var zs                  = moons.map(_.position.z)
    while (xFreq.isEmpty || yFreq.isEmpty || zFreq.isEmpty) {
      if (xFreq.isEmpty && check(xs, next.moons.map(_.position.x), next.moons.map(_.velocity.x))) {
        xFreq = Some(steps)
      }
      if (yFreq.isEmpty && check(ys, next.moons.map(_.position.y), next.moons.map(_.velocity.y))) {
        yFreq = Some(steps)
      }
      if (zFreq.isEmpty && check(zs, next.moons.map(_.position.z), next.moons.map(_.velocity.z))) {
        zFreq = Some(steps)
      }
      next = next.step()
      steps += 1
    }
    (for {
      x <- xFreq
      y <- yFreq
      z <- zFreq
    } yield lcm(x, y, z)).get
  }

  def lcm(a: Long, b: Long, c: Long): Long = {
    def gcd(a: Long, b: Long): Long = if (b == 0) a.abs else gcd(b, a % b)

    def lcm(a: Long, b: Long): Long = (a * b).abs / gcd(a, b)

    lcm(lcm(a, b), c)
  }
  def executeMany(steps: Int): Day12TheNBodyProblem = {
    if (steps == 0) this
    else step().executeMany(steps - 1)
  }

  def step(): Day12TheNBodyProblem = {
    def update(moon: Moon, other: List[Moon]): Moon = other match {
      case m2 :: xs => update(moon.updateVelocity(m2), xs)
      case Nil      => moon
    }

    Day12TheNBodyProblem(
      moons
        .map(m => update(m, moons.filter(_ != m)))
        .map(_.updatePosition()))
  }
}

object Day12TheNBodyProblem {

  def fromString(in: String): Day12TheNBodyProblem = {
    val linePattern = """<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""".r
    Day12TheNBodyProblem(
      in.split("\n")
        .map {
          case linePattern(x, y, z) => Moon(Position(x.toInt, y.toInt, z.toInt), Velocity(0, 0, 0))
        }
        .toList)
  }

  def fromStringWithVelocity(in: String): Day12TheNBodyProblem = {
    val linePattern = """pos=<x=\s*(-?\d+), y=\s*(-?\d+), z=\s*(-?\d+)>, vel=<x=\s*(-?\d+), y=\s*(-?\d+), z=\s*(-?\d+)>""".r
    Day12TheNBodyProblem(
      in.split("\n")
        .map {
          case linePattern(x, y, z, vx, vy, vz) =>
            Moon(Position(x.toInt, y.toInt, z.toInt), Velocity(vx.toInt, vy.toInt, vz.toInt))
        }
        .toList)
  }
}
case class Position(x: Int, y: Int, z: Int)

case class Velocity(x: Int, y: Int, z: Int)

case class Moon(position: Position, velocity: Velocity) {
  import Math.abs
  def totalEnergy(): Int =
    (abs(position.x) + abs(position.y) + abs(position.z)) * (abs(velocity.x) + abs(velocity.y) + abs(velocity.z))

  def updatePosition(): Moon =
    Moon(Position(
           position.x + velocity.x,
           position.y + velocity.y,
           position.z + velocity.z
         ),
         velocity)

  def updateVelocity(that: Moon): Moon =
    Moon(
      position,
      Velocity(
        this.velocity.x + increment(this.position.x, that.position.x),
        this.velocity.y + increment(this.position.y, that.position.y),
        this.velocity.z + increment(this.position.z, that.position.z)
      )
    )

  private def increment(pos1: Int, pos2: Int): Int =
    if (pos1 == pos2) 0
    else if (pos1 > pos2) -1
    else 1
}
