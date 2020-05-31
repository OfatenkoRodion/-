package models

import java.awt.Color
import java.util.UUID

import settings.GlobalSettings

case class JShape(id: UUID, points: Seq[JPoint])

object JShape {
  def o: JShape = {
    JShape(id = UUID.randomUUID(),
      Seq(JPoint(UUID.randomUUID(), GlobalSettings.BlockSize, GlobalSettings.BlockSize, Color.GREEN),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize, GlobalSettings.BlockSize * 2, Color.GREEN),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, GlobalSettings.BlockSize * 2, Color.GREEN),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, GlobalSettings.BlockSize, Color.GREEN)))
  }

  def i: JShape = {
    JShape(id = UUID.randomUUID(),
      Seq(JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize * 3, Color.RED),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize * 2, Color.RED),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize, Color.RED),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, 0, Color.RED)))
  }

  def l: JShape = {
    JShape(id = UUID.randomUUID(),
      Seq(JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize * 3, Color.BLUE),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize * 2, Color.BLUE),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize, Color.BLUE),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 3, -GlobalSettings.BlockSize, Color.BLUE)))
  }

  def t: JShape = {
    JShape(id = UUID.randomUUID(),
      Seq(JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize * 2, Color.YELLOW),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, -GlobalSettings.BlockSize, Color.YELLOW),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 3, -GlobalSettings.BlockSize, Color.YELLOW),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize, -GlobalSettings.BlockSize, Color.YELLOW)))
  }

  def z: JShape = {
    JShape(id = UUID.randomUUID(),
      Seq(JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 3, GlobalSettings.BlockSize, Color.PINK),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize, GlobalSettings.BlockSize * 2, Color.PINK),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, GlobalSettings.BlockSize * 2, Color.PINK),
        JPoint(UUID.randomUUID(), GlobalSettings.BlockSize * 2, GlobalSettings.BlockSize, Color.PINK)))
  }
}
