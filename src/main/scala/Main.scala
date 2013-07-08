package RxTests

import scalabucks.{Cliente, Barista, Caixa}

object Main{
  def main(args: Array[String]) {
//    val rxTests = new RxTests
//    rxTests.calc

    val caixa = new Caixa
    val barista = new Barista(caixa)
    val cliente = new Cliente
    val cliente2 = new Cliente

    cliente.fazerPedido("Expresso", caixa)
    cliente.esperarPedido(barista)

    cliente2.fazerPedido("Capuccino", caixa)
    cliente2.esperarPedido(barista)
  }
}