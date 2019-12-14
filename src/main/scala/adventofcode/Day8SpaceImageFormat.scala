package adventofcode

object Day8SpaceImageFormat {

  def checkSum(image: String, width: Int, height: Int): Int = {
    image
      .grouped(width)
      .grouped(height)
      .map(_.mkString)
      .map(PixelsRow)
      .reduce(
        (left, right) =>
          if (left.zero < right.zero) left
          else right
      )
      .checkSum
  }

  def decodeAndPrint(image: String, width: Int, height: Int): String = {
    val res = decodeInternal(image, width, height)

    for {
      (c, idx) <- res.zipWithIndex
    } yield {
      if(idx % width == 0) println()
      if(c == '1') print("O")
      else print(' ')

    }

    res
  }
  private def decodeInternal(image: String, width: Int, height: Int): String = {
    //0 is black,
    // 1 is white,
    // 2 is transparent.
    image
      .grouped(width)
      .grouped(height)
      .map(_.mkString)
      .reduce(
        (first, second) => {
          first
            .zip(second)
            .map {
              case (c1, c2) =>
                if (c1 == '2') c2
                else c1
            }
            .mkString
        }
      )
  }

  case class PixelsRow(line: String) {
    val zero = line.count(_ == '0')
    val one  = line.count(_ == '1')
    val two  = line.count(_ == '2')

    def checkSum = one * two
  }
}
