package RxTests

import rx.Observable
import rx.Observer
import rx.Subscription
import rx.operators.OperationInterval
import java.util.concurrent.TimeUnit

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

    //garcom.take(10).subscribe(eu)

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
}