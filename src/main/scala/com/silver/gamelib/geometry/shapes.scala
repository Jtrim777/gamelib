package com.silver.gamelib.geometry

import java.awt
import java.awt.{BasicStroke, Color, Graphics2D, Stroke}

case class Rect(width: Int, height: Int, fill: Option[Color] = None,
                stroke: Option[Color] = None, strokeWidth: Float = 1) extends Shape {
  override val strokeColor: Option[Color] = stroke
  override val fillColor: Option[Color] = fill

  override def drawOn(graphics: Graphics2D, pos: DiscretePoint): Unit = {
    val ofill = graphics.getPaint
    val ostroke = graphics.getStroke

    fillColor match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.fillRect(pos.x, pos.y, width, height)
      case None =>
        graphics.setPaint(Shape.CLEAR_COLOR)
    }

    stroke match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.setStroke(new BasicStroke(strokeWidth))
        graphics.drawRect(pos.x, pos.y, width, height)
      case None =>
    }

    graphics.setPaint(ofill)
    graphics.setStroke(ostroke)
  }

}

case class RoundedRect(width: Int, height: Int, cornerRadius: Int, fill: Option[Color] = None,
                stroke: Option[Color] = None, strokeWidth: Float = 1) extends Shape {
  override val strokeColor: Option[Color] = stroke
  override val fillColor: Option[Color] = fill

  override def drawOn(graphics: Graphics2D, pos: DiscretePoint): Unit = {
    val ofill = graphics.getPaint
    val ostroke = graphics.getStroke

    fillColor match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.fillRoundRect(pos.x, pos.y, width, height, cornerRadius/2, cornerRadius/2)
      case None =>
        graphics.setPaint(Shape.CLEAR_COLOR)
    }

    stroke match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.setStroke(new BasicStroke(strokeWidth))
        graphics.drawRoundRect(pos.x, pos.y, width, height, cornerRadius/2, cornerRadius/2)
      case None =>
    }

    graphics.setPaint(ofill)
    graphics.setStroke(ostroke)
  }

}

case class Ellipse(width: Int, height: Int, fill: Option[Color] = None,
                stroke: Option[Color] = None, strokeWidth: Float = 1) extends Shape {
  override val strokeColor: Option[Color] = stroke
  override val fillColor: Option[Color] = fill

  override def drawOn(graphics: Graphics2D, pos: DiscretePoint): Unit = {
    val ofill = graphics.getPaint
    val ostroke = graphics.getStroke

    fillColor match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.fillOval(pos.x, pos.y, width, height)
      case None =>
        graphics.setPaint(Shape.CLEAR_COLOR)
    }

    stroke match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.setStroke(new BasicStroke(strokeWidth))
        graphics.drawOval(pos.x, pos.y, width, height)
      case None =>
    }

    graphics.setPaint(ofill)
    graphics.setStroke(ostroke)
  }
}

class Polygon(points: List[Point[_]], fill: Option[Color] = None,
                   stroke: Option[Color] = None, override val strokeWidth: Float = 1) extends Shape {
  override val strokeColor: Option[Color] = stroke
  override val fillColor: Option[Color] = fill

  override def drawOn(graphics: Graphics2D, pos: DiscretePoint): Unit = {
    val ofill = graphics.getPaint
    val ostroke = graphics.getStroke

    val polygon = new awt.Polygon(
      points.map(p => (p + pos).asDiscrete.x).toArray,
      points.map(p => (p + pos).asDiscrete.y).toArray,
      points.length
    )

    fillColor match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.fillPolygon(polygon)
      case None =>
        graphics.setPaint(Shape.CLEAR_COLOR)
    }

    stroke match {
      case Some(value) =>
        graphics.setPaint(value)
        graphics.setStroke(new BasicStroke(strokeWidth))
        graphics.drawPolygon(polygon)
      case None =>
    }

    graphics.setPaint(ofill)
    graphics.setStroke(ostroke)
  }
}


case class Triangle(p1: DiscretePoint, p2: DiscretePoint, p3: DiscretePoint,
                    fill: Option[Color] = None,
                    stroke: Option[Color] = None,
                    override val strokeWidth: Float = 1) extends Polygon(List(p1,p2,p3), fill, stroke, strokeWidth)
