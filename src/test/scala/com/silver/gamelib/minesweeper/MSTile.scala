package com.silver.gamelib.minesweeper

import com.silver.gamelib.geometry.DiscretePoint
import com.silver.gamelib.geometry.PointUtils._

case class MSTile(pos: DiscretePoint, provider: DiscretePoint => Option[MSTile], mined: Boolean) {
  import MSTile._
  var state: State = Hidden()

  lazy val dangerNeighbors: Int = {
    getNeighbors.foldLeft(0) { (ti,tile) =>
      (if (tile.mined) 1 else 0) + ti
    }
  }

  def toggleFlag(): Unit = {
    state match {
      case Hidden() => this.state = Flagged()
      case Flagged() => this.state = Hidden()
      case Shown() => // Ignore
    }
  }

  def show(): Unit = {
    state match {
      case Hidden() => revealNow()
      case _ => // Ignore
    }
  }

  protected def revealNow(): Unit = {
    this.state = Shown()
    if (dangerNeighbors == 0) {
      getNeighbors.foreach(_.show())
    }
  }

  private def getNeighbors: List[MSTile] = {
    (pos - (1,1)).asDiscrete.to((pos + (1,1)).asDiscrete).asList.filter(_ != pos).flatMap(provider)
  }

  def hidden: Boolean = {
    state == Hidden() || state == Flagged()
  }

  def shown: Boolean = !hidden

  def flagged: Boolean = state == Flagged()
}

object MSTile {
  sealed trait State
  case class Hidden() extends State
  case class Flagged() extends State
  case class Shown() extends State
}
