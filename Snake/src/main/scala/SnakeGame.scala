import java.awt.{Color, Component}

import javax.swing.JFrame

class SnakeGame {

  def run: Unit = {
    val frame = JFrameFactory.create
    val listenerFirst = new WASDListener(reactToKey(_, x = 20, y = 20, frame = frame))
    frame.addKeyListener(listenerFirst)
  }

  private def reactToKey(key: HandledKey, x: Int, y: Int, frame: JFrame): Unit = {
    val (newX, newY) = key match {
      case W => (x, y - 10)
      case A => (x - 10, y)
      case S => (x, y + 10)
      case D => (x + 10, y)
      case Q => System.exit(0)
        throw new Exception("Game over")
    }
    revalidate(frame, newX, newY, new JElement(newX, newY, Color.GREEN, 10, 10, true))
  }

  private def revalidate(frame: JFrame, headX: Int, headY: Int, elementsToDraw: Component*): Unit = {

    val listener = new WASDListener(reactToKey(_, x = headX, y = headY, frame = frame))
    frame.getKeyListeners.foreach(frame.removeKeyListener)
    frame.addKeyListener(listener)

    frame.getContentPane.removeAll()
    elementsToDraw.map { e =>
      frame.getContentPane.add(e)
    }
    frame.revalidate()
  }
}