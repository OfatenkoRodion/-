package actors

import akka.actor.{Actor, Props}
import models.JShape

object GreatRandomActor {
  case object NextShape
}

class GreatRandomActor extends Actor {

  import GreatRandomActor._

  private val forceOfGravityActor = context.actorOf(Props[ForceOfGravityActor])
  forceOfGravityActor ! ForceOfGravityActor.Init


  override def receive: Receive = {
    case NextShape =>
       forceOfGravityActor ! ForceOfGravityActor.AddOrReplaceShapes(generateNextShape)
  }

  private def generateNextShape: JShape = {

    val seed = new java.util.Date().hashCode
    val rand = new scala.util.Random(seed)
    val someNum = rand.nextInt(5) + 1

    someNum match {
      case 1 => JShape.o
      case 2 => JShape.i
      case 3 => JShape.l
      case 4 => JShape.t
      case 5 => JShape.z
    }
  }
}
