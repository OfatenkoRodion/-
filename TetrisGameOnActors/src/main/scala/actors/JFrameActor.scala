package actors

import java.awt.Graphics
import java.awt.event.{KeyEvent, KeyListener}
import java.awt.event.KeyEvent.{VK_A, VK_D, VK_Q, VK_S, VK_W}

import akka.actor.{Actor, ActorRef}
import javax.swing.{JComponent, JFrame}
import models.{A, D, HandledKey, JPoint}
import settings.JFrameFactory


object JFrameActor {

  case object Init

  case class Points(points: Seq[JPoint])

  case class KeyPressedEvent(key: HandledKey)

}

class JFrameActor extends Actor {

  import JFrameActor._

  private val frame: JFrame = JFrameFactory.create
  frame.addKeyListener(new KeyListener {
    def keyPressed(e: KeyEvent): Unit = {
      e.getKeyCode match {
        case VK_A => self ! KeyPressedEvent(A)
        case VK_D => self ! KeyPressedEvent(D)
        case _ => ()
      }
    }
    def keyReleased(keyEvent: KeyEvent): Unit = ()
    override def keyTyped(keyEvent: KeyEvent): Unit = ()
  })

  override def receive: Receive = {
    case Init => context.become(redrawPointsHandler(sender()))
  }

  def redrawPointsHandler(producer: ActorRef): Receive = {
    case Points(points) => redrawPoints(points)
    case event: KeyPressedEvent => producer ! event
  }

  private def redrawPoints(points: Seq[JPoint]): Unit = {
    frame.getContentPane.removeAll()

    frame.add {
      new JComponent {
        override def paint(g: Graphics): Unit = {
          points.foreach { p =>
            g.setColor(p.color)
            g.fillRect(p.xAxis, p.yAxis, p.width, p.height)

            p.borderColor.foreach { bcolor =>
              g.setColor(bcolor)
              g.drawRect(p.xAxis, p.yAxis, p.width, p.height)
            }
          }
        }
      }
    }
    frame.revalidate()
  }
}
