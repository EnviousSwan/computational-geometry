package compgeom.pointinpolygon.ui

import compgeom.convexhull.simplepolygon.ui.Visualizer._

import scala.swing.BorderPanel.Position.{North, South}
import scala.swing.event.{ButtonClicked, MouseClicked}
import scala.swing.{BorderPanel, Button, FlowPanel, MainFrame, SimpleSwingApplication, ToggleButton}

object Visualizer extends SimpleSwingApplication {

	val addPoints = new ToggleButton("Add Points")
	val polygon = new Button("Build Polygon")

	val panel = new FlowPanel(polygon, addPoints)
	val canvas = new PiPCanvas

	def top = new MainFrame {

		title = "Ray-casting - Point in Polygon"

		contents = new BorderPanel {
			layout(panel) = North
			layout(canvas) = South
		}

		listenTo(addPoints, polygon, quickHull)
		listenTo(canvas.mouse.clicks)

		reactions += {
			case e: MouseClicked if addPoints.selected =>
				canvas.addPoint(e.point.x, e.point.y)

			case e: MouseClicked =>
				canvas.setPoint(e.point.x, e.point.y)

			case ButtonClicked(`polygon`) =>
				canvas.buildPolygon()
		}
	}
}
