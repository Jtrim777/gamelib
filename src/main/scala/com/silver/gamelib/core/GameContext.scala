package com.silver.gamelib.core

import com.silver.gamelib.geometry.Bounds
import com.silver.gamelib.graphics.Scene
import com.silver.gamelib.input.Keys.KeyEvent
import com.silver.gamelib.input.Mouse.MouseEvent

trait GameContext[Event] {
  val contextId: ContextKey
  val windowKey: Int
  var isTicking: Boolean
  var receivesInput: Boolean
  var hidden: Boolean = false
  private var updateHook: Option[ContextKey => Unit] = None
  private var eventHook: Option[(ContextKey, Event) => Unit] = None

  def disable(): Unit = {
    this.isTicking = false
    this.receivesInput = false
  }

  def enable(): Unit = {
    this.isTicking = true
    this.receivesInput = true
  }

  def pause(): Unit = {
    this.isTicking = false
  }

  def resume(): Unit = {
    this.isTicking = true
  }

  def unfocus(): Unit = {
    this.receivesInput = false
  }

  def focus(): Unit = {
    this.receivesInput = true
  }

  def show(): Unit = {
    this.hidden = false
  }

  def hide(): Unit = {
    this.hidden = true
  }

  def setHooks(update: ContextKey => Unit, event: (ContextKey, Event) => Unit): Unit = {
    this.updateHook = Some(update)
    this.eventHook = Some(event)
  }

  protected def requestUpdate(): Unit =
    this.updateHook match {
      case Some(value) => value(contextId)
      case None => throw new IllegalStateException(s"Draw update requested by context $contextId " +
        s"before being hooked into a game controller")
    }

  protected def postEvent(target: ContextKey, event: Event): Unit =
    this.eventHook match {
      case Some(value) => value(target, event)
      case None => throw new IllegalStateException(s"Event post requested by context $contextId " +
        s"before being hooked into a game controller")
    }

  // Methods that subclasses must implement
  def getScene: Scene
  def windowZ: Int

  def update(): Unit
  def receiveEvent(event: Event): Unit

  def getInputBounds: Bounds[_]
  def handleMouseEvent(event: MouseEvent): Unit
  def handleKeyEvent(event: KeyEvent): Unit

  def destroy(): Unit
}
