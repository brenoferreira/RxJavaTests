package scalabucks

/**
 * Created with IntelliJ IDEA.
 * User: brenoferreira
 * Date: 7/7/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */

import rx.{Observable}
import rx.subjects.{ReplaySubject}

case class Token(val pedido:String, val token:Int)
case class Bebida(val nome:String, token:Int)

class Scalabucks(caixa:Caixa) {

  private var barista:Barista = null

  def registrarPedido(pedido:String) = {
    caixa.registrarPedido(pedido)
  }

  def setBarista(barista:Barista) = this.barista = barista

  def observarPedidosFeitos = caixa.observar

  def observarPedidosProntos = barista.observar
}

class Caixa {

  val subject = ReplaySubject.create[Token]()

  def observar:Observable[Token] = subject

  def registrarPedido(pedido:String) = {
    val token = Token(pedido, pedido.hashCode)

    subject.onNext(token)

    token
  }
}

class Cliente(scalabucks:Scalabucks) {
  var token:Int = 0

  def fazerPedido(pedido:String) = {
    token = scalabucks.registrarPedido(pedido).token
  }

  def esperarPedido(barista:Barista) = {
    scalabucks
      .observarPedidosProntos
      .filter((bebida:Bebida) => bebida.token == token)
      .subscribe((bebida:Bebida) => println("bebendo " + bebida.nome))
  }
}

class Barista(val scalabucks:Scalabucks) {
  val subject = ReplaySubject.create[Bebida]()

  scalabucks
    .observarPedidosFeitos
    .subscribe((token:Token) => fazerCafe(token))

  def observar:Observable[Bebida] = subject

  def fazerCafe(token:Token) = {
    Thread.sleep(2000)

    subject.onNext(new Bebida(token.pedido, token.token))
  }
}
