package com.silver.gamelib.graphics

import java.awt.{Color, Font, Graphics2D, RenderingHints}

import com.silver.gamelib.geometry.{DiscretePoint, Point, PointUtils}

case class SceneText(value: String, pos: DiscretePoint, zPos: Int) extends GraphicsObject {
  var color: Color = Color.BLACK
  var font: Option[Font] = None
  var bold: Boolean = false

  def renderOn(graphics: Graphics2D, translate: Point[_] = PointUtils.ORIGIN): Unit = {
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON)

    graphics.setPaint(color)

    val useFont = if (font.isDefined) {
      font
    } else if (bold) {
      Some(graphics.getFont.deriveFont(Font.BOLD))
    } else {
      None
    }

    useFont.foreach { f => graphics.setFont(f) }

    val metrics = useFont.map(graphics.getFontMetrics).getOrElse(graphics.getFontMetrics)
    val x = translate.asDiscrete.x + pos.x - (metrics.stringWidth(value)/2)
    val y = translate.asDiscrete.y + pos.y + (metrics.getHeight/4)

    graphics.drawString(value, x, y)
  }

  def withFont(nf: Font): SceneText = {
    this.font = Some(nf)
    this
  }

  def withColor(nc: Color): SceneText = {
    this.color = nc
    this
  }

  def makeBold(): SceneText = {
    this.bold = true
    this
  }
}
