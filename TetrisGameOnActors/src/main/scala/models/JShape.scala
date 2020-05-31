package models

import java.awt.Color
import java.util.UUID

case class JShape(id: UUID, points: Seq[JPoint])

object JShape {
  def o: JShape = {
    JShape(id = UUID.randomUUID(), Seq(JPoint(UUID.randomUUID(), 10, 10, Color.GREEN), JPoint(UUID.randomUUID(), 10, 20, Color.GREEN), JPoint(UUID.randomUUID(), 20, 20, Color.GREEN), JPoint(UUID.randomUUID(), 20, 10, Color.GREEN)))
  }
  def i: JShape = {
    JShape(id = UUID.randomUUID(), Seq(JPoint(UUID.randomUUID(), 20, -30, Color.RED), JPoint(UUID.randomUUID(), 20, -20, Color.RED), JPoint(UUID.randomUUID(), 20, -10, Color.RED), JPoint(UUID.randomUUID(), 20, 0, Color.RED)))
  }

  def l: JShape = {
    JShape(id = UUID.randomUUID(), Seq(JPoint(UUID.randomUUID(), 20, -30, Color.BLUE), JPoint(UUID.randomUUID(), 20, -20, Color.BLUE), JPoint(UUID.randomUUID(), 20, -10, Color.BLUE), JPoint(UUID.randomUUID(), 30, -10, Color.BLUE)))
  }

  def t: JShape = {
    JShape(id = UUID.randomUUID(), Seq(JPoint(UUID.randomUUID(), 20, -20, Color.YELLOW), JPoint(UUID.randomUUID(), 20, -10, Color.YELLOW), JPoint(UUID.randomUUID(), 30, -10, Color.YELLOW), JPoint(UUID.randomUUID(), 10, -10, Color.YELLOW)))
  }

  def z: JShape = {
    JShape(id = UUID.randomUUID(), Seq(JPoint(UUID.randomUUID(), 30, 10, Color.PINK), JPoint(UUID.randomUUID(), 10, 20, Color.PINK), JPoint(UUID.randomUUID(), 20, 20, Color.PINK), JPoint(UUID.randomUUID(), 20, 10, Color.PINK)))
  }
}
