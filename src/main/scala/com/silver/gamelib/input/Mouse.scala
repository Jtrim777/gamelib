package com.silver.gamelib.input

import com.silver.gamelib.core.GameContext
import com.silver.gamelib.geometry.{Bounds, DiscretePoint}

object Mouse {
  sealed trait MouseEvent {
    val button: MouseButton
    val position: DiscretePoint

    def relativize(context: GameContext[_]): MouseEvent
  }
  case class Click(button: MouseButton, position: DiscretePoint) extends MouseEvent {
    override def relativize(context: GameContext[_]): MouseEvent =
      Click(button, (position - context.getInputBounds.origin).asDiscrete)
  }
  case class Pressed(button: MouseButton, position: DiscretePoint) extends MouseEvent {
    override def relativize(context: GameContext[_]): MouseEvent =
      Pressed(button, (position - context.getInputBounds.origin).asDiscrete)
  }
  case class Released(button: MouseButton, position: DiscretePoint) extends MouseEvent {
    override def relativize(context: GameContext[_]): MouseEvent =
      Released(button, (position - context.getInputBounds.origin).asDiscrete)
  }
  case class Entered(button: MouseButton, position: DiscretePoint) extends MouseEvent {
    override def relativize(context: GameContext[_]): MouseEvent =
      Entered(button, (position - context.getInputBounds.origin).asDiscrete)
  }
  case class Exited(button: MouseButton, position: DiscretePoint) extends MouseEvent {
    override def relativize(context: GameContext[_]): MouseEvent =
      Exited(button, (position - context.getInputBounds.origin).asDiscrete)
  }
  case class Moved(button: MouseButton, position: DiscretePoint) extends MouseEvent {
    override def relativize(context: GameContext[_]): MouseEvent =
      Moved(button, (position - context.getInputBounds.origin).asDiscrete)
  }

  sealed trait MouseButton
  case class Right() extends MouseButton
  case class Left() extends MouseButton
  case class Middle() extends MouseButton
//  case class ScrollUp() extends MouseButton
//  case class ScrollDown() extends MouseButton
  case class Unknown() extends MouseButton
}
