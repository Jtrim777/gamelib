package com.silver.gamelib.minesweeper

import scala.util.{Random, Try}

import com.silver.gamelib.geometry.DiscretePoint
import com.silver.gamelib.geometry.PointUtils._

case class MSBoard(width: Int, height: Int, mines: Int) {
  val tiles: List[MSTile] = {
    val mposes = Random.shuffle(0.until(width*height).toList).take(mines)
      .map { i => DiscretePoint(i%width, i/width) }

    val boardBounds = (0,0).to((width-1,height-1))

    boardBounds.map { p =>
      MSTile(p, this.getTile, mposes.contains(p))
    }
  }

  def getTile(pos: DiscretePoint): Option[MSTile] = {
    Try(tiles((pos.y * width) + pos.x)).toOption
  }

  val scoreMultiplier: Double = {
    val tiles = width * height
    val density = mines.toDouble / tiles.toDouble

    tiles.toDouble * 2d * density
  }

  def score: Int = {
    tiles.foldLeft(0){ (t, tile) => t + (if (tile.shown && !tile.mined) 1 else 0) }
  }

  override def toString: String = {
    0.until(height).map { y =>
      val row = 0.until(width).map { x =>
        val tile = getTile((x,y)).get
        if (tile.mined) {
          "X"
        } else {
          tile.dangerNeighbors.toString
        }
      }
      row.mkString("|")
    }.mkString(s"\n${"-".repeat(width*2-1)}\n")
  }
}
