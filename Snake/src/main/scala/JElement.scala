import java.awt.Color

import javax.swing.JComponent
import java.awt.Graphics

class JElement(xAxis: Int, yAxis: Int, color: Color, width: Int, height: Int, isFill: Boolean) extends JComponent {

  override def paint(g: Graphics): Unit = {
    g.setColor(color)
    if (isFill) g.fillRect(xAxis, yAxis, width, height)
    else g.drawRect(xAxis, yAxis, width, height)
  }
}