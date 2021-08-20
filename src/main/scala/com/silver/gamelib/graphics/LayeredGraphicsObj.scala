package com.silver.gamelib.graphics

import java.awt.Graphics2D

import com.silver.gamelib.geometry.Point

case class LayeredGraphicsObj(components: List[GraphicsObject], pos: Point[_], zPos: Int) extends GraphicsObject {
  override def renderOn(graphics: Graphics2D, translate: Point[_]): Unit = {
    components.sortBy(_.zPos).foreach { comp =>
      comp.renderOn(graphics, translate + pos)
    }
  }
}
