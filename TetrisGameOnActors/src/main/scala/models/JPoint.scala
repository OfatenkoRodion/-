package models

import java.awt.Color
import java.util.UUID

import settings.GlobalSettings

/**
  *
  * @param xAxis       - х координата точки
  * @param yAxis       - у координата точки
  * @param color       - основной цвет заливки
  * @param width       - ширина точки
  * @param height      - высота точки
  * @param borderColor - опциональный цвет заливки; так же служит маркером необходимости рисовать границу, если указать в None - граница не будет отображена
  */

case class JPoint(id: UUID, xAxis: Int, yAxis: Int, color: Color, width: Int = GlobalSettings.BlockSize, height: Int = GlobalSettings.BlockSize, borderColor: Option[Color] = Some(Color.BLACK))