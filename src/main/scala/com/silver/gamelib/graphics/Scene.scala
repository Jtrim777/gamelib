package com.silver.gamelib.graphics

import java.awt.{Color, Graphics2D}

import com.silver.gamelib.geometry._

case class Scene(width: Int, height: Int, var windowOffset: Point[_] = PointUtils.ORIGIN) {
  var backgroundColor: Color = Color.BLACK
  var cornerRadius: Int = 0
  var objects: List[GraphicsObject] = List.empty

  def addShape(shape: Shape, pos: Point[_], zPos: Int = 0): Unit = {
    this.objects = SceneShape(shape, pos, zPos) :: objects
  }

  def addText(value: String, pos: Point[_], zPos: Int = 0): Unit = {
    this.objects = SceneText(value, pos.asDiscrete, zPos) :: objects
  }

  def addObject(obj: GraphicsObject): Unit = {
    this.objects = obj :: objects
  }

  def addSubCanvas(scene: Scene, zPos: Int): Unit = {
    this.objects = SubScene(scene, zPos) :: objects
  }

  def renderOn(graphics: Graphics2D, translate: Point[_] = PointUtils.ORIGIN): Unit = {
    graphics.setPaint(this.backgroundColor)
    if (cornerRadius == 0) {
      graphics.fillRect(windowOffset.asDiscrete.x, windowOffset.asDiscrete.y, width, height)
    } else {
      graphics.fillRoundRect(windowOffset.asDiscrete.x, windowOffset.asDiscrete.y, width, height,
        cornerRadius/2, cornerRadius/2)
    }

    this.objects.sortBy({o => o.zPos}).foreach { o => o.renderOn(graphics, windowOffset) }
  }
}

case class SubScene(scene: Scene, zPos: Int) extends GraphicsObject {
  override def renderOn(graphics: Graphics2D, translate: Point[_] = PointUtils.ORIGIN): Unit =
    scene.renderOn(graphics, translate)
}
