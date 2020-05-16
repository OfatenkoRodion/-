import java.awt.event.{KeyEvent, KeyListener}
import KeyEvent._

class WASDListener(callBack: HandledKey => Unit) extends KeyListener {

  def keyPressed(e: KeyEvent): Unit = {
    val key = e.getKeyCode

    key match {
      case VK_W => callBack(W)
      case VK_A => callBack(A)
      case VK_S => callBack(S)
      case VK_D => callBack(D)
      case VK_Q => callBack(Q)
    }
  }

  def keyReleased(keyEvent: KeyEvent): Unit = ()
  override def keyTyped(keyEvent: KeyEvent): Unit = ()
}
