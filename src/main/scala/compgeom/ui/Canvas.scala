package compgeom.ui

import java.awt.Color
import java.awt.geom.{Ellipse2D, Line2D}

import compgeom.geometry.{Line, Point}

import scala.swing.{Dimension, Graphics2D, Panel}

class Canvas extends Panel {

	private val (width, height) = (512, 512)

	private val (xmin, xmax) = (0.0, 1.0)
	private val (ymin, ymax) = (0.0, 1.0)

	private val penRadius = 0.01

	preferredSize = new Dimension(width, height)

	override def paintComponent(g: Graphics2D): Unit = {
		g.clearRect(0, 0, size.width, size.height)
		g.setBackground(Color.white)
	}

	private def drawLine(line: Line)(implicit g: Graphics2D): Unit = {
		val Line(Point(x0, y0), Point(x1, y1)) = line

		g.draw(new Line2D.Double(scaleX(x0), scaleY(y0),
			scaleX(x1), scaleY(y1)))
	}

	def drawLines(lines: List[Line])(implicit g: Graphics2D): Unit =
		lines foreach drawLine

	def drawPoint(point: Point)(implicit g: Graphics2D): Unit = {
		val Point(x, y) = point
		val (xs, ys) = (scaleX(x), scaleY(y))
		val r = penRadius
		val scaledPenRadius = (r * width).toFloat

		g.fill(new Ellipse2D.Double(xs - scaledPenRadius / 2,
			ys - scaledPenRadius / 2, scaledPenRadius, scaledPenRadius))
	}

	def drawPoints(points: List[Point])(implicit g: Graphics2D): Unit =
		points.foreach(drawPoint)

	def drawPolygon(points: List[Point])(implicit g: Graphics2D): Unit =
		if (points.nonEmpty) {
			val vertices = points.sliding(2)
			for (List(a, b) <- vertices)
				drawLine(Line(a, b))

			drawLine(Line(points.head, points.last))
		}

	private def scaleX(x: Double) = width * (x - xmin) / (xmax - xmin)

	private def scaleY(y: Double) = height * (ymax - y) / (ymax - ymin)

	private def factorX(w: Double) = w * width / Math.abs(xmax - xmin)

	private def factorY(h: Double) = h * height / Math.abs(ymax - ymin)

	protected def userX(x: Double): Double =
		xmin + x * (xmax - xmin) / width

	protected def userY(y: Double): Double =
		ymax - y * (ymax - ymin) / height
}
