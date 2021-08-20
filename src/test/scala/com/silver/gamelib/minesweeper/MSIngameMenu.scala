package com.silver.gamelib.minesweeper

import java.awt.Color

import com.silver.gamelib.core.{ContextKey, GameContext, IllegalEventException}
import com.silver.gamelib.geometry.{Bounds, DiscreteBounds, DiscretePoint, RoundedRect}
import com.silver.gamelib.graphics.{Scene, SceneText}
import com.silver.gamelib.input.{Keys, Mouse}
import com.silver.gamelib.minesweeper.MSIngameMenu.KEY

case class MSIngameMenu(ww: Int, wh: Int) extends GameContext[MSEvent] {
  override val contextId: ContextKey = KEY
  override val windowKey: Int = Minesweeper.GAME_WINDOW
  override var isTicking: Boolean = false
  override var receivesInput: Boolean = false

  private var stateText: (String, Color) = ("MineSweeper", Color.WHITE)
  private var currentGame: Option[MSBoard] = None

  val RESUME_BUTTON: DiscreteBounds = DiscreteBounds(DiscretePoint(10,30), ww/2 - 20, 40)
  val RESTART_BUTTON: DiscreteBounds = DiscreteBounds(DiscretePoint(10,80), ww/2 - 20, 40)

  override def getScene: Scene = {
    val scene = Scene(ww/2, wh/2, DiscretePoint(ww/4, wh/4))
    scene.cornerRadius = 8

    val topText = SceneText(stateText._1, DiscretePoint(ww/4, 20), 0)
    topText.color = stateText._2
    scene.addObject(topText)

    scene.addShape(RoundedRect(ww/2 - 20, 40, 8, fill = Some(Color.LIGHT_GRAY)), DiscretePoint(10, 30))
    scene.addShape(RoundedRect(ww/2 - 20, 40, 8, fill = Some(Color.LIGHT_GRAY)), DiscretePoint(10, 80))

    val b1Text = SceneText("Resume", DiscretePoint(ww/4, 50), 1)
    val b2Text = SceneText("New Game", DiscretePoint(ww/4, 100), 1)
    b1Text.color = Color.WHITE
    b2Text.color = Color.WHITE
    scene.addObject(b1Text)
    scene.addObject(b2Text)

    scene
  }

  override def windowZ: Int = 1

  override def update(): Unit = {}

  override def receiveEvent(event: MSEvent): Unit = event match {
    case ShowMenu(board, score) =>
      show()
      focus()
      currentGame = Some(board)
      stateText = (s"Current Score: $score", Color.WHITE)
    case GameOver(score) =>
      show()
      focus()
      currentGame = None
      stateText = (s"Game Over. Score: $score", Color.RED)
    case GameWon(score) =>
      show()
      focus()
      currentGame = None
      stateText = (s"You Won! Score: $score", Color.GREEN)
    case _ => new IllegalEventException[MSEvent](event)
  }

  override def getInputBounds: Bounds[_] = DiscreteBounds(DiscretePoint(ww/4,wh/4), ww/2, wh/2)

  override def handleMouseEvent(event: Mouse.MouseEvent): Unit = {
    event match {
      case Mouse.Click(_,_) => // Ignore
      case _ => return
    }

    if (event.button == Mouse.Left()) {
      if (RESUME_BUTTON.contains(event.position) && currentGame.isDefined) {
        unfocus()
        hide()
        postEvent(MSGameplay.KEY, Resume())
      } else if (RESTART_BUTTON.contains(event.position)) {
        unfocus()
        hide()
        postEvent(ContextKey.ROOT, NewGame())
      }
    }
    requestUpdate()
  }

  override def handleKeyEvent(event: Keys.KeyEvent): Unit = {}

  override def destroy(): Unit = {}
}

object MSIngameMenu {
  val KEY: ContextKey = ContextKey("/menus/ingame")
}
