import java.awt.{Color, Component}

import javax.swing.JFrame
import jelements.JSnakeBodyPart

class SnakeGame {

  import SnakeGame.speed


  def run: Unit = {
    val frame = JFrameFactory.create
    val listenerFirst = new WASDListener(reactToKey(_, x = 20, y = 20, snakeLength = 1, frame = frame, randomPoint = JSnakeBodyPart.generate(Color.RED)))
    frame.addKeyListener(listenerFirst)
  }

  private def reactToKey(key: HandledKey, x: Int, y: Int, snakeLength: Int, frame: JFrame, randomPoint: JSnakeBodyPart): Unit = {
    val (newX, newY) = key match {
      case W => (x, y - speed)
      case A => (x - speed, y)
      case S => (x, y + speed)
      case D => (x + speed, y)
      case Q => System.exit(0)
        throw new Exception("Game over")
    }

    val (newSnakeLength, components, newRP) = if (Math.abs(randomPoint.xAxis - newX ) <= 10 && Math.abs(newY - randomPoint.yAxis) <= 10 && Math.abs(randomPoint.xAxis - newX ) >= 0 && Math.abs(newY - randomPoint.yAxis) >= 0) {
      (snakeLength + 1 , frame.getContentPane.getComponents.reverse.tail.reverse, JSnakeBodyPart.generate(Color.RED))
    }
    else (snakeLength, frame.getContentPane.getComponents, randomPoint)

    revalidate(frame, newSnakeLength, teleportation(newX), teleportation(newY), components.takeRight(newSnakeLength) :+ new JSnakeBodyPart(newX, newY, Color.GREEN), newRP)
  }

  private def teleportation(current: Int, elite: Int = 600): Int = {
    if (current > elite) 0
    else if (current < 0) elite else current
  }

  private def revalidate(frame: JFrame, snakeLength: Int, headX: Int, headY: Int, snakeBody: Array[Component], randomPoint: JSnakeBodyPart): Unit = {
    val listener = new WASDListener(reactToKey(_, x = headX, y = headY, snakeLength, frame = frame, randomPoint))
    frame.getKeyListeners.foreach(frame.removeKeyListener)
    frame.addKeyListener(listener)
    frame.getContentPane.removeAll()

    snakeBody.map { e =>
      frame.add(e)
    }

    frame.revalidate()
    frame.add(randomPoint)
    frame.revalidate()
  }
}

object SnakeGame {
  val speed = 10
}