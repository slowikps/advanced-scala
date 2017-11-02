import java.util.concurrent.{Executor, Executors, TimeUnit}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object DeadLockExample extends App {

  val startTime = System.currentTimeMillis()

  def tick(msg: String) = {
    val now = (System.currentTimeMillis() - startTime)*1.0/1000
    println(now + " " + msg + s"[${Thread.currentThread().getName}]")
  }


  implicit val one = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
  implicit val two = one //ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))
  def giveMeSomeFuture = Future {
    tick("About to wait")
    TimeUnit.SECONDS.sleep(1)
    tick("Result ready")
    "Result ready"
  }(two)

  tick("Start")

  def waitForRes(fut: Future[String]) = {
    tick("Await result")
    Await.ready(fut, 1 seconds)
    TimeUnit.SECONDS.sleep(1)
  }

  Future {
    for {
      _ <- 1 to 50
      fut = giveMeSomeFuture
      _ = waitForRes(fut)
    } yield ()
  }(one).map(
    _ => tick("The end")
  )(one)



  TimeUnit.SECONDS.sleep(20)
  println("The end")
}
