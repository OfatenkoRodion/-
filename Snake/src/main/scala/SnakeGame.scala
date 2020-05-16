import java.awt.{Color, Component}

import javax.swing.JFrame
import jelements.JSnakeBodyPart

import scala.util.Random

class SnakeGame {

  import SnakeGame.speed


  def run: Unit = {
    val frame = JFrameFactory.create
    val listenerFirst = new WASDListener(reactToKey(_, x = 20, y = 20, frame = frame, randomPoint = JSnakeBodyPart.generate(Color.RED)))
    frame.addKeyListener(listenerFirst)
  }

  private def reactToKey(key: HandledKey, x: Int, y: Int, frame: JFrame, randomPoint: JSnakeBodyPart): Unit = {
    val (newX, newY) = key match {
      case W => (x, y - speed)
      case A => (x - speed, y)
      case S => (x, y + speed)
      case D => (x + speed, y)
      case Q => System.exit(0)
        throw new Exception("Game over")
    }

    val components = frame.getContentPane.getComponents.takeRight(15)

    revalidate(frame, teleportation(newX), teleportation(newY),  components :+ new JSnakeBodyPart(newX, newY, Color.GREEN) , randomPoint)

  }

  private def teleportation(current: Int, elite: Int = 600): Int = {
    if (current > elite) 0
    else if(current < 0) elite else current
  }

  private def revalidate(frame: JFrame, headX: Int, headY: Int, snakeBody: Array[Component], randomPoint: JSnakeBodyPart): Unit = this.synchronized {
    val listener = new WASDListener(reactToKey(_, x = headX, y = headY, frame = frame, randomPoint))
    frame.getKeyListeners.foreach(frame.removeKeyListener)
    frame.addKeyListener(listener)
    frame.getContentPane.removeAll()

    println(snakeBody.length)

    snakeBody.map { e =>
      frame.add(e)
    }


    frame.revalidate()
  }
}

object SnakeGame {
  val speed = 10
}