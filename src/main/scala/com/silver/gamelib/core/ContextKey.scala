package com.silver.gamelib.core

case class ContextKey(path: String)

object ContextKey {
  val ROOT: ContextKey = ContextKey("/")
}
