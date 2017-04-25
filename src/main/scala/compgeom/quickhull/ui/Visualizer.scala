package compgeom.quickhull.ui

import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, Button, MainFrame, SimpleSwingApplication}

object Visualizer extends SimpleSwingApplication {

	def top = new MainFrame {

		title = "QuickHull"

		val random = new Button { text = "Randomize" }
		val quickHull = new Button { text = "QuickHull" }

		val canvas = new QuickHullCanvas

		contents = new BorderPanel {
			layout(random) = West
			layout(quickHull) = East
			layout(canvas) = South
		}

		listenTo(random, quickHull)

		reactions += {
			case ButtonClicked(component) if component == random =>
				canvas.randomizePoints()
			case ButtonClicked(component) if component == quickHull =>
				canvas.buildQuickHull()
		}
	}
}
