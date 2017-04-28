package compgeom.pointinpolygon.ui

import java.awt.Color

import compgeom.geometry.{Point, Polygon}
import compgeom.pointinpolygon.logic.PointInPolygon
import compgeom.ui.Canvas

import scala.collection.mutable.ListBuffer
import scala.swing.Graphics2D

class PiPCanvas extends Canvas {

	private var isInside = false
	private var point: Point = Point()
	private val points: ListBuffer[Point] = ListBuffer()
	private var polygon: Polygon = List()

	override def paintComponent(g: Graphics2D): Unit = {
		super.paintComponent(g)
		implicit val graphics = g

		g.setColor(Color.black)
		drawPoints(points.toList)
		drawPolygon(polygon)

		if (isInside) g.setColor(Color.red)
		else g.setColor(Color.blue)
		drawPoint(point)
	}

	def buildPolygon(): Unit = {
		polygon = points.toList
		repaint()
	}

	def addPoint(x: Double, y: Double): Unit = {
		points += Point(userX(x), userY(y))
		repaint()
	}

	def setPoint(x: Double, y: Double): Unit = {
		point = Point(userX(x), userY(y))
		isInside = PointInPolygon.isInside(point, polygon)
		repaint()
	}
}
