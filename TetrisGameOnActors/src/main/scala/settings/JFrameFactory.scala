package settings

import java.awt.Dimension
import java.awt.event.{WindowAdapter, WindowEvent}

import javax.swing.JFrame

object JFrameFactory {

  def create: JFrame = {
    val frame = new JFrame()
    frame.setSize(new Dimension(GlobalSettings.Width, GlobalSettings.Height))
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)
    frame.setResizable(false)
    frame.setTitle("TETRIS v1.0 by Rodion Ofatenko")

    frame.addWindowListener(new WindowAdapter {
      override def windowClosing(e: WindowEvent): Unit = {
        System.exit(0)
      }})
    frame
  }
}
