package adventofcode

object Day4SecureContainer {

  def differentPasswordsIn(range: Range)(validator: Int => Boolean): Int =
    range.foldLeft(0)(
      (acc, next) =>
        if (validator(next)) acc + 1
        else acc
    )

  def isCorrectPassword(pass: Int): Boolean = {
    def validate(in: List[Int], hasAdjacent: Boolean): Boolean =
      in match {
        case x :: y :: xs =>
          if (x > y) false
          else if (x == y) validate(y :: xs, true)
          else validate(y :: xs, hasAdjacent)
        case _ => hasAdjacent
      }

    val asList: List[Int] = pass.toString.map(_.toInt).toList

    if (asList.length == 6) validate(asList, false)
    else false
  }

  def isCorrectPasswordV2(pass: Int): Boolean = {
    def validate(in: List[Int], hasAdjacent: Boolean): Boolean =
      in match {
        case x :: y :: xs =>
          if (x > y) false
          else if (x == y && !hasAdjacent) {
            val (same, rest) = xs.span(_ == y)
            validate(y :: rest, same.isEmpty)
          } else validate(y :: xs, hasAdjacent)
        case _ => hasAdjacent
      }

    val asList: List[Int] = pass.toString.map(_.toInt).toList

    if (asList.length == 6) validate(asList, false)
    else false
  }
}
