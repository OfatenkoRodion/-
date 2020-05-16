import java.awt.Dimension
import java.awt.event.{WindowAdapter, WindowEvent}

import javax.swing.JFrame

object JFrameFactory {

  def create: JFrame = {
    val frame = new JFrame()
    frame.setSize(new Dimension(600, 600))
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)

    frame.addWindowListener(new WindowAdapter {
      override def windowClosing(e: WindowEvent): Unit = {
        System.exit(0)
      }})
    frame
  }
}
