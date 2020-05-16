package jelements

import java.awt.{Color, Graphics}

import javax.swing.JComponent

class JSnakeBodyPart(xAxis: Int, yAxis: Int, color: Color = Color.GREEN) extends JComponent {

  override def paint(g: Graphics): Unit = {
    g.setColor(color)
    g.fillRect(xAxis, yAxis, 10, 10)
    g.setColor(Color.BLACK)
    g.drawRect(xAxis, yAxis, 10, 10)
  }
}

object JSnakeBodyPart {
  private val r = new scala.util.Random

  def generate(color: Color): JSnakeBodyPart = {
    val r1 = r.nextInt(600)
    val r2 = r.nextInt(600)

    new JSnakeBodyPart(r1, r2, color)
  }
}

case class Point(xAxis: Int, yAxis: Int, color: Color = Color.GREEN)

class PointsContainer(seq: Seq[Point]) extends JComponent {

  override def paint(g: Graphics): Unit = {
    seq.map { v =>
      g.setColor(v.color)
      g.fillRect(v.xAxis, v.yAxis, 10, 10)
      g.setColor(Color.BLACK)
      g.drawRect(v.xAxis, v.yAxis, 10, 10)
    }
  }
}
