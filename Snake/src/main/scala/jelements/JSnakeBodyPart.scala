package jelements

import java.awt.{Color, Graphics}

import javax.swing.JComponent

case class JSnakeBodyPart(xAxis: Int, yAxis: Int, color: Color = Color.GREEN) extends JComponent {

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
    val r1 = r.nextInt(400) + 100
    val r2 = r.nextInt(400) + 100

    new JSnakeBodyPart(r1, r2, color)
  }
}