package RxTests

import scalabucks.{Scalabucks, Cliente, Barista, Caixa}

object Main{
  def main(args: Array[String]) {
//    val rxTests = new RxTests
//    rxTests.calc

    val caixa = new Caixa
    val scalabucks = new Scalabucks(caixa)

    val barista = new Barista(scalabucks)
    scalabucks.setBarista(barista)

    val cliente = new Cliente(scalabucks)
    val cliente2 = new Cliente(scalabucks)

    cliente.fazerPedido("Expresso")
    cliente.esperarPedido(barista)

    cliente2.fazerPedido("Capuccino")
    cliente2.esperarPedido(barista)
  }
}