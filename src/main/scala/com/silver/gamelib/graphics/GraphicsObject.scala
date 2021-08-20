package com.silver.gamelib.graphics

import java.awt.Graphics2D

import com.silver.gamelib.geometry.{Point, PointUtils}

trait GraphicsObject {
  def renderOn(graphics: Graphics2D, translate: Point[_] = PointUtils.ORIGIN): Unit

  def zPos: Int
}
