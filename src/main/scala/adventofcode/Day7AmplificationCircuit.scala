package adventofcode

import scala.collection.mutable.ArrayBuffer

object Day7AmplificationCircuit {

  import Day5SunnyWithChanceOfAsteroids._
  def maxAmplifier(in: List[Int], phaseSettings: List[Int] = List(0, 1, 2, 3, 4)): Int = {
    def inner(output: Int, phaseSetting: List[Int]): Int = phaseSetting match {
      case x :: xs => inner(intCode(in, x :: output :: Nil), xs)
      case Nil     => output
    }
    (for {
      inputs <- phaseSettings.permutations
    } yield inner(intCode(in, inputs.head :: 0 :: Nil), inputs.tail)).max
  }

  def maxAmplifierWithFeedback(in: List[Int]): Int = {
    def feedback(toCheck: List[Int]): Int = {
      val phaseSettings = {
        val res = toCheck.map(List(_))
        res.init :+ (res.last :+ 0)
      }

      val first = new AmplifierControllerSoftware(in.to(ArrayBuffer), "A", phaseSettings.head)
      val last = ('B' to 'E').zip(phaseSettings.tail).foldLeft(first) {
        case (prev, (name, phaseSettings)) =>
          val next = new AmplifierControllerSoftware(in.to(ArrayBuffer), s"$name", phaseSettings)
          next.inputProvider = prev
          prev.nextController = next
          next
      }
      first.inputProvider = last
      last.nextController = first

      first.nextAction()
      last.next()
    }
    (for {
      inputs <- List(5, 6, 7, 8, 9).permutations
    } yield feedback(inputs)).max
  }
}
trait InputProvider {
  def next(): Int
  def nonEmpty(): Boolean
}

class ParameterManager(args: List[Int]) {
  private val parameters = ArrayBuffer(args: _*)

  def nonEmpty(): Boolean = parameters.nonEmpty
  def next(): Int         = parameters.remove(0)

  def add(param: Int): Unit = parameters += param
}


//Manager parameters for the next Amplifier, get parameters from the previous one
class AmplifierControllerSoftware(private val input: ArrayBuffer[Int], val name: String, nextAmplifierPhaseSettings: List[Int])
    extends ParameterManager(nextAmplifierPhaseSettings)
    with InputProvider {

  var nextController: AmplifierControllerSoftware = null
  var inputProvider: InputProvider                = null

  var nextAction = () => intCode()

  private def intCode(): Unit = {
    def inner(idx: Int): Unit = {
      val pm: ParameterMode = ParameterMode.fromInt(input(idx))
      def getParameter(offset: Int): Int = pm.mode(offset - 1) match {
        case PositionMode  => input(input(idx + offset))
        case ImmediateMode => input(idx + offset)
      }
      pm.opCode match {
        case 3 => // ----- Consume -----
          nextAction = () => {
            input(input(idx + 1)) = inputProvider.next()
            inner(idx + 2)
          }
          if (inputProvider.nonEmpty()) nextAction()
          else nextController.nextAction() //Suspend next action until more input parameters produced
        case 4 => // ----- Produce -----
          add(getParameter(1))
          inner(idx + 2)
        case 99 =>
          nextAction = () => {
            nextAction = () => throw  new IllegalStateException("Should be only called once!")
          }

          println(s"The end: $name")
          nextController.nextAction()

        //Boring stuff
        case 1 =>
          input(input(idx + 3)) = getParameter(1) + getParameter(2)
          inner(idx + 4)
        case 2 =>
          input(input(idx + 3)) = getParameter(1) * getParameter(2)
          inner(idx + 4)
        case 5 =>
          if (getParameter(1) != 0) inner(getParameter(2))
          else inner(idx + 3)
        case 6 =>
          if (getParameter(1) == 0) inner(getParameter(2))
          else inner(idx + 3)
        case 7 =>
          if (getParameter(1) < getParameter(2)) input(input(idx + 3)) = 1
          else input(input(idx + 3)) = 0
          inner(idx + 4)
        case 8 =>
          if (getParameter(1) == getParameter(2)) input(input(idx + 3)) = 1
          else input(input(idx + 3)) = 0
          inner(idx + 4)
        case _ => throw new IllegalStateException("Ops")

      }
    }
    inner(0)
  }
}
