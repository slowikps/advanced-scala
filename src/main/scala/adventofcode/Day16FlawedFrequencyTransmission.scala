package adventofcode

import shapeless.ops.nat.Min

object Day16FlawedFrequencyTransmission {

  private val basePattern = List(0, 1, 0, -1)
//
//  101010
//  0110011
//  00111000111
//  000111100001111
//  0000111110000011111
  def fftFirst8(in: IndexedSeq[Int], phases: Int): String =
    fft(in, phases).take(8).mkString("")

  def fftFirst8(in: String, phases: Int): String =
    fftFirst8(in.map(_.asDigit), phases)

  def fftFirstPartTwo(in: String, phases: Int, repeat: Int, mlt: Int = 1): String = {
    val tmp = in.map(_.asDigit)
    val asDigit: IndexedSeq[Int] =
      IndexedSeq.fill(repeat)(tmp).flatten

    val toDrop = asDigit.take(7).mkString("").toInt
//    fft(asDigit, phases).slice(toDrop, toDrop + 8).mkString("")
    println(fft(asDigit, 1, mlt).mkString(""))
    println(fft(asDigit, 2, mlt).mkString(""))
    println(fft(asDigit, 3, mlt).mkString(""))

    ""
  }

  def fft(inputSignal: IndexedSeq[Int], phases: Int, multipler: Int = 1): IndexedSeq[Int] = {
    val res = (1 to inputSignal.size).map(idx => {
      Math.abs(selectNumbers(idx, inputSignal, multipler)) % 10
    })
    if (phases > 1) fft(res, phases - 1)
    else res
  }
  import Math.min
  def selectNumbers(iteration: Int, inputSignal: IndexedSeq[Int], multipler: Int): Int = {
    var add    = true
    var result = 0
    val inputLength = inputSignal.length
    for (i <- iteration - 1 until inputLength by iteration * 2) {
      if (add) {
        for (idx <- i until min(i + iteration, inputLength)) {
          result += inputSignal(idx) * multipler
        }
      } else {
        for (idx <- i until min(i + iteration, inputLength)) {
          result -= inputSignal(idx) * multipler
        }
      }
      add = !add
    }
    result
  }
}
