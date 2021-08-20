package com.silver.gamelib.core

import java.awt.event.{ActionEvent, WindowAdapter, WindowEvent}

import com.silver.gamelib.graphics.GameWindow
import com.silver.gamelib.input.Keys.KeyEvent
import com.silver.gamelib.input.Mouse.MouseEvent
import com.silver.gamelib.input.{KeyAdapter, MouseAdapter}
import javax.swing.Timer

trait Game[Event] {
  val title: String
  val tickRate: Double

  protected var windows: Map[Int, GameWindow] = Map()
  protected var contexts: Map[ContextKey, GameContext[Event]] = Map(ContextKey.ROOT -> DummyContext())

  protected val timer: Timer = new Timer((tickRate * 1000).toInt,
    (_: ActionEvent) => Game.this.tick())

  def createWindow(key: Int, title: String, width: Int, height: Int): Unit = {
    if (windows.contains(key)) {
      throw new IllegalArgumentException(s"A window with key $key already exists")
    }

    println(s"Creating window $title with size ($width x $height)")
    val newWindow = new GameWindow(width, height, title)
    newWindow.addWindowListener(new WindowAdapter {
      override def windowClosing(e: WindowEvent): Unit = {
        Game.this.windowClosed(key)
        super.windowClosing(e)
      }
    })
    val ma = new MouseAdapter(key, newWindow.frame.getInsets, this.handleMouseEvent)
    newWindow.addMouseListener(ma)
    newWindow.addMouseMotionListener(ma)
    newWindow.addKeyListener(new KeyAdapter(key, this.handleKeyEvent))
    newWindow.setFocusable(true)

    newWindow.open()

    windows = windows + (key -> newWindow)
  }

  def addContext(context: GameContext[Event]): Unit = {
    if (contexts.contains(context.contextId)) {
      throw new IllegalArgumentException(s"Cannot re-add existing context with key ${context.contextId}")
    } else if (!windows.contains(context.windowKey)) {
      throw new IllegalArgumentException(s"Cannot create context in non-existent window ${context.windowKey}")
    }

    context.setHooks(this.renderContext, this.dispatchEvent)

    contexts = contexts + (context.contextId -> context)
  }

  private def windowClosed(id: Int): Unit = {
    windows = windows.filter { case (i, window) => i != id }
    contexts = contexts.flatMap { case (cid, context) =>
      if (context.windowKey == id) {
        context.destroy()
        None
      } else {
        Some(cid -> context)
      }
    }
  }

  protected def renderContext(id: ContextKey): Unit = {
    rerenderWindow(contexts(id).windowKey)
  }

  protected def dispatchEvent(target: ContextKey, event: Event): Unit = {
    if (target == ContextKey.ROOT) {
      this.handleEvent(event)
    } else {
      this.contexts(target).receiveEvent(event)
    }
  }

  protected def rerenderWindow(wkey: Int): Unit = {
    val ctxts = contexts
      .map { case (_, context) => context }
      .filter { context => context.windowKey == wkey && !context.hidden}
      .toList
      .sortBy { c => c.windowZ }

    if (ctxts.isEmpty) return

    val scene = ctxts.head.getScene
    ctxts.tail.foreach { c => scene.addSubCanvas(c.getScene, c.windowZ * 1000000) }

    windows(wkey).render(scene)
  }

  protected def handleMouseEvent(event: MouseEvent, window: Int): Unit = {
    contexts
      .filter { case (_, context) =>
        context.windowKey == window &&
          context.receivesInput &&
          context.getInputBounds.contains(event.position)
      }
      .foreach { case (_, context) =>
        println(s"[Game] Mouse event $event passed to context ${context.contextId.path} in window $window")
        context.handleMouseEvent(event.relativize(context))
      }
  }

  protected def handleKeyEvent(event: KeyEvent, window: Int): Unit = {
    contexts
      .filter { case (_, context) => context.windowKey == window && context.receivesInput }
      .foreach { case (_, context) => context.handleKeyEvent(event) }
  }

  protected def tick(): Unit = {
    contexts
      .values
      .filter { _.isTicking }
      .foreach(_.update())
  }

  def handleEvent(event: Event): Unit

  def start(): Unit = {
    if (tickRate != 0) {
      this.timer.start()
    }
  }

  def quit(): Unit = {
    contexts.foreach { case (_, context) => context.destroy() }
    windows.foreach { case (i, window) => window.close() }
    timer.stop()
  }
}
