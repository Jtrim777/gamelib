package com.silver.gamelib.geometry

import java.awt.{Color, Graphics2D}

trait Shape {
  val strokeWidth: Float
  val strokeColor: Option[Color]
  val fillColor: Option[Color]

  def drawOn(graphics: Graphics2D, pos: DiscretePoint): Unit

//  def contains(point: Point[_]): Boolean
}

object Shape {
  val CLEAR_COLOR = new Color(0,0,0,0)
}
