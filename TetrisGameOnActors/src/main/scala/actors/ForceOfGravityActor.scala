package actors

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import models.{A, D, JPoint, JShape}
import settings.GlobalSettings

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ForceOfGravityActor {

  case object Init

  case class AddOrReplaceShapes(shape: JShape)

  case object RedrawFrame

  private case object TickGravity

}

class ForceOfGravityActor extends Actor {

  import ForceOfGravityActor._

  private val jFrameActor = context.actorOf(Props[JFrameActor])

  context.system.scheduler.schedule(0 seconds, 500 milliseconds)(self ! TickGravity)

  override def receive: Receive = {
    case Init =>
      jFrameActor ! JFrameActor.Init
      context.become(pointsHolder(Map.empty, Seq.empty, sender()))
  }

  def pointsHolder(shapeMap: Map[UUID, Seq[JPoint]], staticPoints: Seq[JPoint], shapeProducer: ActorRef): Receive = {
    case AddOrReplaceShapes(shape) =>
      context.become(pointsHolderWithId(shapeMap + (shape.id -> shape.points), staticPoints, shapeProducer, shape.id))
  }

  def pointsHolderWithId(shapeMap: Map[UUID, Seq[JPoint]], staticPoints: Seq[JPoint], shapeProducer: ActorRef, lastShapeId: UUID): Receive = {
    case AddOrReplaceShapes(shape) =>
      context.become(pointsHolderWithId(shapeMap + (shape.id -> shape.points), staticPoints, shapeProducer, shape.id))

    case RedrawFrame =>
      jFrameActor ! JFrameActor.Points(shapeMap.values.flatten.toSeq ++ staticPoints)

    case TickGravity =>
      val (stillFalling, needStop) = shapeMap.partition {
        case (_, shape) => shape.maxBy(_.yAxis).yAxis < GlobalSettings.Height - GlobalSettings.BlockSize * 2
      }

      val (needStop2, stillFalling2) = stillFalling.partition {
        case (_, shape) => shape.exists { s =>
          isPointInList(s.copy(yAxis = s.yAxis + GlobalSettings.BlockSize), staticPoints)
        }
      }

      val newMap = stillFalling2.map {
        case (key, shape) => key -> shape.map(s => s.copy(yAxis = s.yAxis + GlobalSettings.BlockSize))
      }


      if ((needStop ++ needStop2).nonEmpty) shapeProducer ! GreatRandomActor.NextShape

      val (needRestart, newStaticPoints) = checkFullLine(staticPoints ++ needStop.flatMap(_._2) ++ needStop2.flatMap(_._2))

      println("needRestart:"+ needRestart)

      if (needRestart)
        context.become(pointsHolderWithId(newMap + (UUID.randomUUID() -> newStaticPoints), Seq.empty, shapeProducer, lastShapeId))
       else context.become(pointsHolderWithId(newMap, newStaticPoints, shapeProducer, lastShapeId))

      self ! RedrawFrame

    case event: JFrameActor.KeyPressedEvent =>
      val shapeOpt = shapeMap.get(lastShapeId)

      val shapeMovedOpt = event.key match {
        case A => shapeOpt.map { shape =>
          shape.map(s => s.copy(xAxis = s.xAxis - GlobalSettings.BlockSize))
        }
        case D => shapeOpt.map { shape =>
          shape.map(s => s.copy(xAxis = s.xAxis + GlobalSettings.BlockSize))
        }
      }
      shapeMovedOpt.foreach { shape =>
        if (shape.forall(_.xAxis >= 0) && shape.forall(_.xAxis <= GlobalSettings.Width - GlobalSettings.BlockSize)) {
          context.become(pointsHolderWithId(shapeMap + (lastShapeId -> shape), staticPoints, shapeProducer, lastShapeId))
          self ! RedrawFrame
        }
      }
  }

  private def isPointInList(point: JPoint, points: Seq[JPoint]): Boolean = {
    points.exists(p => (point.yAxis == p.yAxis) && (point.xAxis == p.xAxis))
  }

  private def checkFullLine(staticPoints: Seq[JPoint]): (Boolean, Seq[JPoint]) = {
    if (staticPoints.nonEmpty) {
      val groupped = staticPoints.groupBy { p =>
        p.yAxis
      }
      val resultList = groupped.filterNot {
        case (_, values) => values.length == 15
      }.values.flatten.toSeq
      (staticPoints.length != resultList.length, resultList)
    } else (false, staticPoints)
  }
}
