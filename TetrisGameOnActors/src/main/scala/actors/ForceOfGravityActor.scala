package actors

import java.util.UUID

import akka.actor.{Actor, Props}
import models.{JPoint, JShape}
import settings.GlobalSettings

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ForceOfGravityActor {

  case class AddOrReplaceShapes(shape: JShape)
  case object RedrawFrame
  case object TickGravity

}

class ForceOfGravityActor extends Actor {

  import ForceOfGravityActor._

  val jFrameActor = context.actorOf(Props[JFrameActor])

  context.system.scheduler.schedule(0 seconds, 100 milliseconds)(self ! TickGravity)

  override def receive: Receive = pointsHolder(Map.empty, Seq.empty)

  def pointsHolder(shapeMap: Map[UUID, Seq[JPoint]], staticPoints: Seq[JPoint]): Receive = {
    case AddOrReplaceShapes(shape) =>
      context.become(pointsHolder(shapeMap + (shape.id -> shape.points), staticPoints))

    case RedrawFrame =>
      jFrameActor ! JFrameActor.Points(shapeMap.values.flatten.toSeq ++ staticPoints)

    case TickGravity =>
      val (stillFalling, needStop) = shapeMap.partition {
        case (_, shape) => shape.maxBy(_.yAxis).yAxis < GlobalSettings.Height - 50 // TODO WTF is 50 ?
      }

      val newMap = stillFalling.map {
        case (key, shape) => key -> shape.map(s => s.copy(yAxis = s.yAxis + GlobalSettings.BlockSize))
      }

      context.become(pointsHolder(newMap, staticPoints ++ needStop.flatMap(_._2)))
      self ! RedrawFrame
  }
}
