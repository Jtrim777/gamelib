package com.silver.gamelib.input

object Keys {
  sealed trait KeyPosition
  case class Normal() extends KeyPosition
  case class Left() extends KeyPosition
  case class Right() extends KeyPosition
  case class Numpad() extends KeyPosition

  sealed trait KeyEvent {
//    def apply(code: String, position: KeyPosition): KeyEvent

    def code: String
    def position: KeyPosition
  }

  object KeyPosition {
    def getForEvent(event: java.awt.event.KeyEvent): KeyPosition =
      event.getKeyLocation match {
        case 2 => Left()
        case 3 => Right()
        case 4 => Numpad()
        case _ => Normal()
      }
  }

  case class Pressed(code: String, position: KeyPosition) extends KeyEvent
  case class Released(code: String, position: KeyPosition) extends KeyEvent
}
