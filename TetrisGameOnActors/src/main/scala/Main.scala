import java.awt.Color
import java.util.UUID

import actors.ForceOfGravityActor
import actors.ForceOfGravityActor.AddOrReplaceShapes
import akka.actor.{ActorSystem, Props}
import models.{JPoint, JShape}

object Main extends App {

  val system = ActorSystem("system")

  val frameActor = system.actorOf(Props[ForceOfGravityActor])

  frameActor ! AddOrReplaceShapes(JShape(UUID.randomUUID(), Seq(JPoint(UUID.randomUUID(), 10, 10, Color.GREEN), JPoint(UUID.randomUUID(), 10, 20, Color.GREEN), JPoint(UUID.randomUUID(), 20, 20, Color.GREEN), JPoint(UUID.randomUUID(), 20, 10, Color.GREEN))))

}
