package com.silver.gamelib.minesweeper

sealed trait MSEvent
case class ShowMenu(board: MSBoard, score: Int) extends MSEvent
case class GameOver(score: Int) extends MSEvent
case class GameWon(score: Int) extends MSEvent
case class NewGame() extends MSEvent
case class Resume() extends MSEvent
case class Quit() extends MSEvent