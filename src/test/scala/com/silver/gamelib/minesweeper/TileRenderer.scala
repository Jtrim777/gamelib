package com.silver.gamelib.minesweeper

import java.awt.Color

import com.silver.gamelib.geometry.{DiscretePoint, Ellipse, Rect, Shape, Triangle}
import com.silver.gamelib.graphics.{GraphicsObject, LayeredGraphicsObj, SceneShape, SceneText}
import com.silver.gamelib.geometry.PointUtils._

object TileRenderer {
  def renderTile(tile: MSTile): GraphicsObject = {
    val baseShape = Some(SceneShape(
      Rect(Minesweeper.TILE_SIZE, Minesweeper.TILE_SIZE,
        fill = Some(if (tile.shown) Color.DARK_GRAY else Color.LIGHT_GRAY),
        stroke = Some(if (tile.shown) Color.LIGHT_GRAY else Color.DARK_GRAY), strokeWidth = 0.5f),
      (0,0),
      0
    ))

    val icon: Option[SceneShape] = if (tile.shown && tile.mined) {
      Some(SceneShape(
        Ellipse(Minesweeper.TILE_SIZE/3, Minesweeper.TILE_SIZE/3, fill = Some(Color.RED)),
        (Minesweeper.TILE_SIZE/2, Minesweeper.TILE_SIZE/2),
        1
      ))
    } else if (tile.flagged) {
      Some(SceneShape(
        Triangle(
          (Minesweeper.TILE_SIZE/2, Minesweeper.TILE_SIZE/3),
          (Minesweeper.TILE_SIZE/3, 2*Minesweeper.TILE_SIZE/3),
          (2*Minesweeper.TILE_SIZE/3, 2*Minesweeper.TILE_SIZE/3),
          fill = Some(Color.ORANGE)
        ),
        (0, 0),
        1
      ))
    } else {
      None
    }

    val text: Option[SceneText] = if (tile.dangerNeighbors > 0 && tile.shown) {
      val col = tile.dangerNeighbors match {
        case 1 => Color.BLUE
        case 2 => Color.GREEN
        case _ => Color.RED
      }

      Some(
        SceneText(
          tile.dangerNeighbors.toString,
          (Minesweeper.TILE_SIZE / 2, Minesweeper.TILE_SIZE / 2),
          2
        ).withColor(col).makeBold()
      )
    } else {
      None
    }

    LayeredGraphicsObj(
      List(baseShape, icon, text).flatten,
      (tile.pos.x * Minesweeper.TILE_SIZE, tile.pos.y * Minesweeper.TILE_SIZE),
      0
    )
  }
}
