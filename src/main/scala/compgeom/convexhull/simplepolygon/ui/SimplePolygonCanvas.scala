package compgeom.convexhull.simplepolygon.ui

import java.awt.Color

import compgeom.convexhull.simplepolygon.logic.SimplePolygonHull
import compgeom.geometry.Point
import compgeom.ui.Canvas

import scala.collection.mutable.ListBuffer
import scala.swing.Graphics2D

class SimplePolygonCanvas extends Canvas {

	private var points: ListBuffer[Point] = ListBuffer()
	private var convexHull: List[Point] = List()
	private var polygon: List[Point] = List()

	override def paintComponent(g: Graphics2D): Unit = {
		implicit val graphics = g

		g.setColor(Color.black)
		drawPoints(points.toList)
		drawPolygon(polygon)

		g.setColor(Color.red)
		drawPolygon(convexHull)
	}

	def addPoint(x: Double, y: Double): Unit = {
		points += Point(userX(x), userY(y))
		repaint()
	}

	def buildPolygon(): Unit = {
		polygon = points.toList
		repaint()
	}

	def buildConvexHull(): Unit = {
		convexHull = SimplePolygonHull.build(points.toList)
		repaint()
	}
}
