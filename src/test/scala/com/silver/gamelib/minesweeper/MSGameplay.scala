package com.silver.gamelib.minesweeper

import com.silver.gamelib.core.{ContextKey, GameContext, IllegalEventException}
import com.silver.gamelib.geometry.{Bounds, DiscreteBounds, DiscretePoint, PointUtils}
import com.silver.gamelib.graphics.Scene
import com.silver.gamelib.input.{Keys, Mouse}
import com.silver.gamelib.minesweeper.MSGameplay.KEY

case class MSGameplay(board: MSBoard, w: Int, h: Int) extends GameContext[MSEvent] {
  override val contextId: ContextKey = KEY
  override val windowKey: Int = Minesweeper.GAME_WINDOW
  override var isTicking: Boolean = false
  override var receivesInput: Boolean = true

  {
    println(board.toString)
  }

  override def getScene: Scene = {
    val scene = Scene(w,h)

    board.tiles.foreach { tile =>
      scene.addObject(TileRenderer.renderTile(tile))
    }

    scene
  }

  override def windowZ: Int = 0

  override def update(): Unit = {}

  override def receiveEvent(event: MSEvent): Unit = {
    event match {
      case Resume() =>
        focus()
      case _ => throw new IllegalEventException[MSEvent](event)
    }
  }

  override def getInputBounds: Bounds[_] = {
    DiscreteBounds(PointUtils.ORIGIN, w, h)
  }

  override def handleMouseEvent(event: Mouse.MouseEvent): Unit = {
    event match {
      case Mouse.Click(_,_) => // Ignore
      case _ => return
    }

    val tpos = DiscretePoint(event.position.x / Minesweeper.TILE_SIZE,
      event.position.y / Minesweeper.TILE_SIZE)

//    println(s"Click at ${event.position}; Cell index $tpos")

    val otile = board.getTile(tpos)

    otile match {
      case Some(tile) => event.button match {
        case Mouse.Right() => if (tile.hidden) tile.toggleFlag()
        case Mouse.Left() =>
          tile.show()
          if (tile.mined && !tile.flagged) {
            unfocus()
            postEvent(MSIngameMenu.KEY, GameOver((board.score * board.scoreMultiplier).toInt))
          } else if (board.score == (board.width*board.height - board.mines)) {
            unfocus()
            postEvent(MSIngameMenu.KEY, GameWon((board.score * board.scoreMultiplier).toInt))
          }
        case Mouse.Middle() =>
        case Mouse.Unknown() =>
      }
      case None => return
    }

    requestUpdate()
  }

  override def handleKeyEvent(event: Keys.KeyEvent): Unit = {
    if (event.code == "ESC") {
      unfocus()
      postEvent(MSIngameMenu.KEY, ShowMenu(board, (board.score * board.scoreMultiplier).toInt))
    }
  }

  override def destroy(): Unit = {}
}

object MSGameplay {
  val KEY: ContextKey = ContextKey("/gameplay")
}
