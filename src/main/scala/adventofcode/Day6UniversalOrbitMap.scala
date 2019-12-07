package adventofcode

object Day6UniversalOrbitMap {
  //I should use tre instead!
  def orbitsToSanta(in: List[String]): Int = {
    val ancestors =
      in.map(str => {
          val split = str.split("\\)")
          (split(0), split(1))
        })
        .groupBy(_._2)
        .mapValues(_.map(_._1))
        .mapValues(_.headOption)
        .filter(_._2.isDefined)
        .view.mapValues(_.getOrElse("")) //Filter line above!

    def toParentMap(elem: String, level: Int): Map[String, Int] =
      ancestors
        .get(elem)
        .map(parent => {
          toParentMap(parent, level + 1) + (parent -> level)
        })
        .getOrElse(Map.empty)

    def commonAncestorDistance(elem: String, otherParents: Map[String, Int]): Int =
      otherParents.getOrElse(elem, 1 + commonAncestorDistance(ancestors(elem), otherParents))

    commonAncestorDistance("SAN", toParentMap("YOU", -1))
  }

  def countOrbits(in: List[String]): Int = countOrbits("COM", 0, asMap(in).toMap)

  private def asMap(in: List[String]) = {
    val grouped =
      in.map(str => {
          val split = str.split("\\)")
          (split(0), split(1))
        })
        .groupBy(_._1)
        .view.mapValues(_.map(_._2))
    grouped
  }

  private def countOrbits(elem: String, orbits: Int, map: Map[String, List[String]]): Int = {
    map
      .getOrElse(elem, Nil)
      .foldLeft(orbits) {
        case (acc, next) => acc + countOrbits(next, orbits + 1, map)
      }
  }
}
