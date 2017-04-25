package compgeom.quickhull.ui

import compgeom.geometry.{Line, Point}
import compgeom.quickhull.logic.QuickHull
import compgeom.ui.Canvas

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.swing.Graphics2D
import scala.util.Random

class QuickHullCanvas extends Canvas {

	private val setSize = 20
	private var points: List[Point] = List()
	private var lines: ListBuffer[Line] = ListBuffer()

	override def paintComponent(g: Graphics2D): Unit = {
		implicit val graphics = g

		drawPoints(points)
		drawLines(lines.toList)
	}

	def randomizePoints(): Unit = {
		points = generatePoints
		lines.clear()
		repaint()
	}

	def buildQuickHull(): Unit = {

		import scala.concurrent.ExecutionContext.Implicits.global

		val (_, linesToDraw) = QuickHull.convexHull(points)

		linesToDraw.zipWithIndex foreach { case (line, i) =>
			Future {
				Thread.sleep(i * 100 + 100)
				lines += line
			} map (_ => repaint())
		}
	}

	private def generatePoints: List[Point] =
		List.fill(setSize)(
			Point(Random.nextDouble(), Random.nextDouble()))
}
