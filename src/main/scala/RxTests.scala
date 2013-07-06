package RxTests

import rx.{Observable, Observer}
import rx.operators.OperationInterval
import java.util.concurrent.TimeUnit
import rx.subscriptions.{BooleanSubscription, Subscriptions}
import rx.concurrency.Schedulers

class RxTests{
  def helloWorld = {
    val observable = Observable.from(1, 2, 3)

    observable.subscribe((n:Int) => println(n))
  }

  def churrasco = {
    val cozinha = Array("picanha", "coracao", "linguica", "frango")

    def carneRandom = {
      val random = new scala.util.Random()
      val r = random.nextInt(cozinha.length)
      cozinha(r)
    }

    val garcom = Observable
                  .create(OperationInterval.interval(1, TimeUnit.SECONDS))
                  .map((n:Long) => carneRandom)

    val eu = new Object with Observer[String]{
      def onNext (item:String) = println(item)
      def onError (e:Exception) = println(e.getMessage)
      def onCompleted = println("Chega!")
    }

    val quantidadePicanha = garcom
      .take(10)
      .aggregate(0, (acc:Int, carne:String) => {
        println(carne)
        carne match {
          case "picanha" => acc + 1
          case _ => acc
        }
      })

    quantidadePicanha.subscribe((qtd:Int) => println("Comeu picanha " + qtd + " vezes"))
  }

  def calc = {
    val repl = new Object with Iterable[String] {
      def iterator = new Object with Iterator[String]{
        def hasNext = true
        def next = readLine()
      }
    }

    val calculator = Observable
      .create((observer:Observer[String]) => {
        for(str <- repl) observer.onNext(str)
      })
      .takeWhile((line:String) => line != "end")
      .map((input:String) => {
        val x = input.split(' ')

        (x(0).toInt, x(1), x(2).toInt)
      })
      .map((d:(Int, String, Int)) => {
        val op1 = d._1
        val op2 = d._3
        val op = d._2

        op match {
          case "+" => op1 + op2
          case "-" => op1 - op2
          case "/" => op1 / op2
          case "*" => op1 * op2
        }
      })


    calculator
    .subscribeOn(Schedulers.newThread())
    .subscribe(
      (result:Int) => println(result),
      (ex:Exception) => println(ex.getMessage))
  }
}