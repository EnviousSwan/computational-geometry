package compgeom.kdtree.ui

import compgeom.geometry.Point

import scala.swing.BorderPanel.Position._
import scala.swing.event.{MouseClicked, MouseDragged, MousePressed, MouseReleased}
import scala.swing.{BorderPanel, FlowPanel, MainFrame, SimpleSwingApplication, ToggleButton}

object Visualizer extends SimpleSwingApplication {

	var start: Point = _
	var end: Point = _

	val KDTree = new ToggleButton("KDTree")
	val range = new ToggleButton("Range")

	val panel = new FlowPanel(KDTree, range)
	val canvas = new KDTreeCanvas

	def top = new MainFrame {

		title = "KDTree"

		contents = new BorderPanel {
			layout(panel) = North
			layout(canvas) = South
		}

		listenTo(canvas.mouse.clicks, canvas.mouse.moves)

		reactions += {
			case e: MouseClicked =>
				canvas.addPoint(e.point.x, e.point.y, KDTree.selected)

			case e: MousePressed if range.selected =>
				start = Point(e.point.x, e.point.y)

			case e: MouseDragged if range.selected =>
				end = Point(e.point.x, e.point.y)
				canvas.drawRange(start, end)

			case e: MouseReleased if range.selected =>
				end = Point(e.point.x, e.point.y)
				canvas.drawRange(start, end)
		}
	}
}
