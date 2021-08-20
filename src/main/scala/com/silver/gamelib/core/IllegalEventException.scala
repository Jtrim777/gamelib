package com.silver.gamelib.core

case class IllegalEventException[E](event: E)
  extends RuntimeException(s"The event $event was invalid for the target context")
