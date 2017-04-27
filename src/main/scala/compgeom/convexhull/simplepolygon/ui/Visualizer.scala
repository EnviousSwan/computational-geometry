package compgeom.convexhull.simplepolygon.ui

import scala.swing.BorderPanel.Position.{North, South}
import scala.swing.event.{ButtonClicked, MouseClicked}
import scala.swing.{BorderPanel, Button, FlowPanel, MainFrame, SimpleSwingApplication, ToggleButton}

object Visualizer extends SimpleSwingApplication {

	val addPoints = new ToggleButton("Add Points")
	val polygon = new Button("Build Polygon")
	val quickHull = new Button("Convex Hull")

	val panel = new FlowPanel(polygon, addPoints, quickHull)
	val canvas = new SimplePolygonCanvas

	def top = new MainFrame {

		title = "Convex Hull of Simple Polygon"

		contents = new BorderPanel {
			layout(panel) = North
			layout(canvas) = South
		}

		listenTo(addPoints, polygon, quickHull)
		listenTo(canvas.mouse.clicks)

		reactions += {
			case e: MouseClicked if addPoints.selected =>
				canvas.addPoint(e.point.x, e.point.y)

			case ButtonClicked(`polygon`) if !addPoints.selected =>
				canvas.buildPolygon()

			case ButtonClicked(`quickHull`) if !addPoints.selected =>
				canvas.buildConvexHull()
		}
	}
}
