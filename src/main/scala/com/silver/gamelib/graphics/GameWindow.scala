package com.silver.gamelib.graphics

import java.awt.event.{KeyAdapter, MouseAdapter, WindowAdapter, WindowEvent}
import java.awt.{BorderLayout, Dimension, Graphics2D}

import javax.swing.{JFrame, WindowConstants}

object GameWindow {
  var WINDOWS_OPEN: Int = 0
}

class GameWindow(val width: Int, val height: Int, val title: String) {
  private val windowAdapter: WindowAdapter = new WindowAdapter {
    override def windowClosing(e: WindowEvent): Unit = {
      GameWindow.WINDOWS_OPEN -= 1
      GameWindow.this.drawPanel.clearPanel()
      if (GameWindow.WINDOWS_OPEN <= 0) {
        System.exit(0)
      }
      super.windowClosing(e)
    }
  }

  private val drawPanel: CanvasPanel = {
    val y = new CanvasPanel(width, height)
    y.addNotify()

    y
  }

  private[gamelib] val frame: JFrame = {
    val x = new JFrame(title)
    x.setLayout(new BorderLayout())
    x.setResizable(false)
    x.addWindowListener(windowAdapter)
    x.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    x.getContentPane.add(drawPanel, "Center")
    x.setSize(width, height)
    x.getContentPane.setMinimumSize(new Dimension(width, height))
    x.pack()
    x.update(drawPanel.getGraphics)
    x.setVisible(false)

    x
  }

  def open(): Unit = {
    if (!this.frame.isVisible) {
      GameWindow.WINDOWS_OPEN += 1
      this.frame.setVisible(true)
    }
  }

  def close(): Unit = {
    if (this.frame.isVisible) {
      GameWindow.WINDOWS_OPEN -= 1
      this.frame.setVisible(false)
    }
  }

  def clear(): Unit = this.drawPanel.clearPanel()

  def render(scene: Scene): Unit = {
    if (scene.width != this.width || scene.height != this.height) {
      throw new IllegalArgumentException(s"The provided scene of size " +
        s"[${scene.width}, ${scene.height}] does not match the canvas size " +
        s"[$width, $height]")
    }

    if (this.frame.getWidth < scene.width || this.frame.getHeight < scene.height) {
      this.frame.getContentPane.setMinimumSize(new Dimension(scene.width, scene.height))
    }

    this.clear()
    this.drawPanel.drawScene(scene)
  }

  // MARK: Delegate methods
  def addWindowListener(l: WindowAdapter): Unit = frame.addWindowListener(l)
  def addMouseListener(l: MouseAdapter): Unit = frame.addMouseListener(l)
  def addMouseMotionListener(l: MouseAdapter): Unit = frame.addMouseMotionListener(l)
  def addKeyListener(l: KeyAdapter): Unit = frame.addKeyListener(l)
  def setFocusTraversalKeysEnabled(v: Boolean): Unit = frame.setFocusTraversalKeysEnabled(v)
  def setFocusable(v: Boolean): Unit = frame.setFocusable(v)
}
