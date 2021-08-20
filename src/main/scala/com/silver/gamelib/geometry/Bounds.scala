package com.silver.gamelib.geometry

import PointUtils._

sealed trait Bounds[D] {
  val origin: Point[D]
  val width: D
  val height: D

  def contains(point: Point[_]): Boolean
}

case class PinBounds(origin: Point[Double], width: Double, height: Double) extends Bounds[Double] {
  def contains(point: Point[_]): Boolean = {
    point.asPin.x < origin.asPin.x + width &&
      point.asPin.y < origin.asPin.y + height
  }
}

case class DiscreteBounds(origin: Point[Int], width: Int, height: Int) extends Bounds[Int] {
  def contains(point: Point[_]): Boolean = {
    point.asPin.x < origin.asPin.x + width.toDouble &&
      point.asPin.y < origin.asPin.y + height.toDouble
  }

  def map[R](func: DiscretePoint => R): List[R] = {
    origin.y.to(origin.y + height).flatMap { y =>
      origin.x.to(origin.x + width).map { x =>
        func((x,y))
      }
    }.toList
  }

  def asList: List[DiscretePoint] = this.map(p => p)

  def foreach(func: DiscretePoint => Unit): Unit = {
    this.asList.foreach(func)
  }

  def foldLeft[B](start: B)(func: (B, DiscretePoint) => B): B = {
    this.asList.foldLeft(start)(func)
  }
}
