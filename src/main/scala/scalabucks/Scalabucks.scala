package scalabucks

/**
 * Created with IntelliJ IDEA.
 * User: brenoferreira
 * Date: 7/7/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */

import rx.{Observable, Observer}
import rx.subjects.{Subject, ReplaySubject}

case class Token(val pedido:String, val token:Int)
case class Bebida(val nome:String, token:Token)

class Caixa {

  val subject = ReplaySubject.create[Token]()

  def observar:Observable[Token] = subject

  def registrarPedido(pedido:String) = {
    val token = Token(pedido, pedido.hashCode)

    subject.onNext(token)

    token
  }
}

class Cliente() {

  var token:Token = null

  def fazerPedido(pedido:String, caixa:Caixa) = {
    token = caixa.registrarPedido(pedido)
  }

  def esperarPedido(barista:Barista) = {
    barista.observar
      .filter((bebida:Bebida) => bebida.token == token)
      .subscribe((bebida:Bebida) => println("bebendo " + bebida.nome))
  }
}

class Barista(val caixa:Caixa) {
  val subject = ReplaySubject.create[Bebida]()

  caixa.observar.subscribe((token:Token) => fazerCafe(token))

  def observar:Observable[Bebida] = subject

  def fazerCafe(token:Token) = {
    Thread.sleep(2000)

    subject.onNext(new Bebida(token.pedido, token))
  }
}
