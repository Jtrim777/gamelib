package com.silver.gamelib.minesweeper

import com.silver.gamelib.core.{Game, IllegalEventException}

object Minesweeper extends Game[MSEvent] {
  override val title: String = "Minesweeper"
  override val tickRate: Double = 0

  var boardWidth: Int = -1
  var boardHeight: Int = -1
  var mineCount: Int = -1

  val GAME_WINDOW = 0
  val TILE_SIZE = 30


  override def start(): Unit = {
    createWindow(GAME_WINDOW, title, TILE_SIZE * boardWidth, TILE_SIZE * boardHeight)
    addContext({
      val c = MSIngameMenu(TILE_SIZE * boardWidth, TILE_SIZE * boardHeight)
      c.hide()
      c
    })

    if (boardWidth > 0 && boardHeight > 0 && mineCount >= 0) {
      addContext(MSGameplay(MSBoard(boardWidth, boardHeight, mineCount),
        TILE_SIZE * boardWidth, TILE_SIZE * boardHeight))
      renderContext(MSGameplay.KEY)
    } else {
      throw new IllegalArgumentException(s"Invalid config params: (w:$boardWidth, h:$boardHeight, m:$mineCount)")
    }

    super.start()
  }

  override def handleEvent(event: MSEvent): Unit = {
    event match {
      case NewGame() =>
        contexts.get(MSGameplay.KEY).foreach { c => c.destroy() }
        contexts = contexts - MSGameplay.KEY

        addContext(MSGameplay(MSBoard(boardWidth, boardHeight, mineCount),
          TILE_SIZE * boardWidth, TILE_SIZE * boardHeight))
        renderContext(MSGameplay.KEY)
      case Quit() =>
        quit()
      case _ => throw new IllegalEventException[MSEvent](event)
    }
  }

  def main(args: Array[String]): Unit = {
    this.boardWidth = args(0).toInt
    this.boardHeight = args(1).toInt
    this.mineCount = args(2).toInt

    this.start()
  }
}
