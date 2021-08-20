package com.silver.gamelib.input
import java.awt.event
import java.awt.event.{KeyEvent => JKeyEvent}

import com.silver.gamelib.input.KeyAdapter.parseKeyEvent
import com.silver.gamelib.input.Keys._

class KeyAdapter(val window: Int, val hook: (KeyEvent, Int) => Unit) extends java.awt.event.KeyAdapter {
  override def keyPressed(e: event.KeyEvent): Unit = {
    parseKeyEvent(e) match {
      case Some((code, pos)) => hook(Pressed(code, pos), window)
      case None => // Ignore
    }

    super.keyPressed(e)
  }

  override def keyReleased(e: event.KeyEvent): Unit = {
    parseKeyEvent(e) match {
      case Some((code, pos)) => hook(Released(code, pos), window)
      case None => // Ignore
    }

    super.keyPressed(e)
  }
}

object KeyAdapter {
  val keyMapping: Map[Int, String] = {
    val fields = classOf[JKeyEvent].getFields

    val parsed = fields.flatMap { f =>
      val name = f.getName

      if (!name.startsWith("VK_")) {
        None
      } else {
        val keyName = name.substring(3)
        val keyCode = (try {
          f.get(null)
        } catch {
          case _: Throwable => throw new IllegalStateException("Couldn't get value of key constant " + keyName)
        }).asInstanceOf[Int]

        Some(keyCode -> keyName)
      }
    }.toList

    Map(parsed:_*)
  }

  def parseKeyEvent(raw: java.awt.event.KeyEvent): Option[(String, KeyPosition)] =
    if (raw.getKeyCode == 0) {
      None
    } else {
      Some(keyMapping(raw.getKeyCode), KeyPosition.getForEvent(raw))
    }
}
