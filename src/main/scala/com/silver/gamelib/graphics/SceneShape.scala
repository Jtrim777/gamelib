package com.silver.gamelib.graphics

import java.awt.{Color, Graphics2D}

import com.silver.gamelib.geometry._
import com.silver.gamelib.geometry.PointUtils._

case class SceneShape(shape: Shape, pos: Point[_], zPos: Int) extends GraphicsObject {
  def renderOn(graphics: Graphics2D, translate: Point[_] = PointUtils.ORIGIN): Unit =
    shape.drawOn(graphics, (pos.asDiscrete + translate).asDiscrete)

//  def setStrokeWidth(value: Int): Unit = shape.strokeWidth = value
//  def setStrokeColor(value: Color): Unit = shape.strokeColor = value
//  def setFillColor(value: Color): Unit = shape.fillColor = value
//  def makeEmpty(): Unit = shape.makeEmpty()
}

