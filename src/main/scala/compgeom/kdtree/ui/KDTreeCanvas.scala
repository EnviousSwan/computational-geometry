package compgeom.kdtree.ui

import java.awt.Color

import compgeom.geometry.{Line, Point, Rectangle}
import compgeom.kdtree.logic.KDTree.Level
import compgeom.kdtree.logic.{Empty, KDTree}
import compgeom.ui.Canvas

import scala.collection.mutable.ListBuffer
import scala.swing.Graphics2D

class KDTreeCanvas extends Canvas {

	private val points: ListBuffer[Point] = ListBuffer()
	private var rangePoints: List[Point] = List()

	private var kdtree: KDTree = Empty
	private var tree: List[(Line, Level)] = List()

	private var range: Rectangle = Rectangle(0, 0, 0, 0)

	override def paintComponent(g: Graphics2D): Unit = {
		implicit val graphics = g
		super.paintComponent(g)

		g.setColor(Color.BLACK)
		drawPoints(points.toList)
		drawRectangle(range)

		drawKDTree(tree)

		g.setColor(Color.RED)
		drawPoints(rangePoints)
	}

	def addPoint(x: Double, y: Double, isTree: Boolean): Unit = {
		val point = Point(userX(x), userY(y))
		points += point

		if (isTree) {
			kdtree = kdtree.insert(point)
			tree = kdtree.draw()
		}

		repaint()
	}

	def drawKDTree(kdtree: List[(Line, Level)])(implicit g: Graphics2D): Unit =
		for ((line, level) <- kdtree) {
			val color =
				if (level == KDTree.VERTICAL) Color.red
				else Color.blue

			g.setColor(color)
			drawLine(line)
		}

	def drawRange(a: Point, b: Point): Unit = {
		val (xmin, xmax) =
			if (a.x < b.x) (a.x, b.x) else (b.x, a.x)

		val (ymin, ymax) =
			if (a.y < b.y) (a.y, b.y) else (b.y, a.y)

		range = Rectangle(xmin, ymin, xmax, ymax)

		val region = Rectangle(userX(xmin), userY(ymax),
			userX(xmax), userY(ymin))
		rangePoints = kdtree.range(region)

		repaint()
	}
}
