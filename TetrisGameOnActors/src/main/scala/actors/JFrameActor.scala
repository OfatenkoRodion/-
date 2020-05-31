package actors

import java.awt.Graphics

import akka.actor.Actor
import javax.swing.{JComponent, JFrame}
import models.JPoint
import settings.JFrameFactory


object JFrameActor {

  case class Points(points: Seq[JPoint])

}

class JFrameActor extends Actor {

  import JFrameActor._

  private val frame: JFrame = JFrameFactory.create

  override def receive: Receive = {
    case Points(points) => redrawPoints(points)
  }

  private def redrawPoints(points: Seq[JPoint]): Unit = {
    frame.getContentPane.removeAll()
    points.foreach { p =>
      frame.add {
        new JComponent {
          override def paint(g: Graphics): Unit = {
            g.setColor(p.color)
            g.fillRect(p.xAxis, p.yAxis, p.width, p.height)

            p.borderColor.foreach { bcolor =>
              g.setColor(bcolor)
              g.drawRect(p.xAxis, p.yAxis, p.width, p.height)
            }
          }
        }
      }
      frame.revalidate()
    }
  }

}
