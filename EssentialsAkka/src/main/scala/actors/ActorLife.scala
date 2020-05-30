package actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

// дефолтное поведение супервизера. Если потомок получит ошибку, то произойдет его автоматический перезапуск.
// в данном примере, если послать след. сообщение in Child то оно нормально отработает
object Start extends App {

  val system = ActorSystem("system")

  val parent = system.actorOf(Props[Parent])

  parent ! FailChild

}

case object Fail
case object FailChild

class Parent extends Actor {

  val child = context.actorOf(Props[Child])

  override def receive: Receive = {
    case FailChild => child ! Fail
  }
}

class Child extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("supervised child started")
  override def postStop(): Unit = log.info("supervised child stopped")


  override def preRestart(reason: Throwable, message: Option[Any]): Unit =
    log.info(s"supervised actor restarting bacouse of ${reason.getMessage}")

  override def postRestart(reason: Throwable): Unit =
    log.info("supervised actor restarted")


  override def receive: Receive = {
    case Fail =>
      log.warning("child will bail now")
      throw new RuntimeException("I failed")
  }

}



