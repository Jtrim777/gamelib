package com.silver.gamelib.graphics

import java.awt.{BorderLayout, Color, Dimension, Graphics, Graphics2D}

import com.silver.gamelib.graphics.CanvasPanel.BufferedDrawAction
import javax.swing.JPanel

class CanvasPanel(val pwidth: Int, val pheight: Int) extends JPanel(new BorderLayout()) {

  {
    this.setPreferredSize(new Dimension(pwidth, pheight))
  }

  private val paintAction = BufferedDrawAction()

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
//    println("Redrawing primary panel")
    paintAction.sink(g.asInstanceOf[Graphics2D])
  }

  def clearPanel(base:Color = Color.WHITE): Unit = {
//    paintAction.fill { graphics =>
//      graphics.setPaint(base)
//      graphics.drawRect(0,0,pwidth,pheight)
//    }

    repaint()
  }

  def drawScene(scene: Scene): Unit = {
    paintAction.fill { graphics =>
      graphics.setPaint(scene.backgroundColor)
      graphics.drawRect(0,0,pwidth,pheight)

      scene.renderOn(graphics)
    }

    repaint()
  }
}

object CanvasPanel {
  case class BufferedDrawAction(var action: Option[Graphics2D => Unit] = None) {
    def fill(func: Graphics2D => Unit): Unit = {
      this.action = Some(func)
    }

    def sink(graphics: Graphics2D): Unit = {
      this.action.foreach { action =>
//        println("Executing draw action")
        action(graphics)
      }
      this.action = None
    }
  }
}
