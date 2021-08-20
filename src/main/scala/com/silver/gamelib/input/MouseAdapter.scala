package com.silver.gamelib.input

import java.awt.{Insets, event}

import com.silver.gamelib.geometry.DiscretePoint
import com.silver.gamelib.input.Mouse._
import com.silver.gamelib.input.MouseAdapter.{getButtonForEvent, getEventPos}
import javax.swing.SwingUtilities

class MouseAdapter(val window: Int, insets: Insets, val hook: (MouseEvent, Int) => Unit) extends java.awt.event.MouseAdapter {
  override def mouseClicked(e: event.MouseEvent): Unit =
    hook(Click(getButtonForEvent(e), adjust(getEventPos(e))), window)

  override def mousePressed(e: event.MouseEvent): Unit =
    hook(Pressed(getButtonForEvent(e), adjust(getEventPos(e))), window)

  override def mouseReleased(e: event.MouseEvent): Unit =
    hook(Released(getButtonForEvent(e), adjust(getEventPos(e))), window)

//  override def mouseEntered(e: event.MouseEvent): Unit =
//    hook(Entered(getButtonForEvent(e), adjust(getEventPos(e))), window)
//
//  override def mouseExited(e: event.MouseEvent): Unit =
//    hook(Exited(getButtonForEvent(e), adjust(getEventPos(e))), window)
//
//  override def mouseMoved(e: event.MouseEvent): Unit =
//    hook(Moved(getButtonForEvent(e), adjust(getEventPos(e))), window)

  private def adjust(p: DiscretePoint): DiscretePoint = {
    DiscretePoint(p.x - insets.left, p.y - insets.top)
  }
}

object MouseAdapter {
  def getButtonForEvent(event: java.awt.event.MouseEvent): MouseButton =
    if (SwingUtilities.isLeftMouseButton(event)) {
      Left()
    } else if (SwingUtilities.isRightMouseButton(event)) {
      Right()
    } else if (SwingUtilities.isMiddleMouseButton(event)) {
      Middle()
    } else {
      Unknown()
    }

  def getEventPos(event: java.awt.event.MouseEvent): DiscretePoint =
    DiscretePoint(event.getX, event.getY)
}