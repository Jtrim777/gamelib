package com.silver.gamelib.core
import com.silver.gamelib.geometry.{Bounds, DiscreteBounds, PointUtils}
import com.silver.gamelib.graphics.Scene
import com.silver.gamelib.input.{Keys, Mouse}

case class DummyContext[E]() extends GameContext[E] {
  override val contextId: ContextKey = ContextKey.ROOT
  override val windowKey: Int = -1
  override var isTicking: Boolean = false
  override var receivesInput: Boolean = false

  {
    this.hidden = true
  }

  override def getScene: Scene = {
    Scene(0,0)
  }

  override def windowZ: Int = -1

  override def update(): Unit = {

  }

  override def receiveEvent(event: E): Unit = {}

  override def getInputBounds: Bounds[_] = DiscreteBounds(PointUtils.ORIGIN, 0,0)

  override def handleMouseEvent(event: Mouse.MouseEvent): Unit = {

  }

  override def handleKeyEvent(event: Keys.KeyEvent): Unit = {

  }

  override def destroy(): Unit = {

  }
}
