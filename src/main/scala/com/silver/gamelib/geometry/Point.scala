package com.silver.gamelib.geometry
import PointUtils._

sealed trait Point[D] {
  def x: D
  def y: D

  def asPin: PinPoint
  def asDiscrete: DiscretePoint

  def +(other: Point[_]): Point[_]

  def -(other: Point[_]): Point[_]
}
case class DiscretePoint(x: Int, y: Int) extends Point[Int] {
  override def asPin: PinPoint = PinPoint(x.toDouble, y.toDouble)

  override def asDiscrete: DiscretePoint = this

  def to(other: DiscretePoint): DiscreteBounds = {
    val start = if(x <= other.x && y <= other.y) this else other
    val end = if (start == this) other else this

    DiscreteBounds(start, end.x - start.x, end.y - start.y)
  }

  override def +(other: Point[_]): Point[_] = other match {
    case DiscretePoint(ox, oy) => DiscretePoint(x+ox, y+oy)
    case PinPoint(ox, oy) => PinPoint(x.toDouble+ox, y.toDouble+oy)
  }

  override def -(other: Point[_]): Point[_] = other match {
    case DiscretePoint(ox, oy) => DiscretePoint(x-ox, y-oy)
    case PinPoint(ox, oy) => PinPoint(x.toDouble-ox, y.toDouble-oy)
  }
}
case class PinPoint(x: Double, y: Double) extends Point[Double] {
  override def asPin: PinPoint = this

  override def asDiscrete: DiscretePoint = DiscretePoint(x.round.toInt, y.round.toInt)

  override def +(other: Point[_]): Point[_] = other match {
    case DiscretePoint(ox, oy) => PinPoint(x+ox, y+oy)
    case PinPoint(ox, oy) => PinPoint(x+ox, y+oy)
  }

  override def -(other: Point[_]): Point[_] = other match {
    case DiscretePoint(ox, oy) => PinPoint(x-ox, y-oy)
    case PinPoint(ox, oy) => PinPoint(x-ox, y-oy)
  }
}

object PointUtils {
  implicit def mkPoint(tuple: (Int, Int)): DiscretePoint = DiscretePoint(tuple._1, tuple._2)

  implicit def mkPoint(tuple: (Double, Double)): PinPoint = PinPoint(tuple._1, tuple._2)

  implicit def cvtPoint(pt: DiscretePoint): PinPoint = pt.asPin
  implicit def cvtPoint(pt: PinPoint): DiscretePoint = pt.asDiscrete

  val ORIGIN = DiscretePoint(0,0)
}
