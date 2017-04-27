package compgeom.convexhull.quickhull.ui

import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, Button, FlowPanel, MainFrame, SimpleSwingApplication}

object Visualizer extends SimpleSwingApplication {

	def top = new MainFrame {

		title = "QuickHull"

		val random = new Button("Randomize")
		val quickHull = new Button("QuickHull")
		val panel = new FlowPanel(random, quickHull)

		val canvas = new QuickHullCanvas

		contents = new BorderPanel {
			layout(panel) = North
			layout(canvas) = South
		}

		listenTo(random, quickHull)

		reactions += {
			case ButtonClicked(`random`) => canvas.randomizePoints()
			case ButtonClicked(`quickHull`) => canvas.buildQuickHull()
		}
	}
}
