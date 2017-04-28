package compgeom.pointinpolygon.logic

import compgeom.geometry.{Line, Point, Polygon}
import compgeom.geometry

object PointInPolygon {

	private val xmax = 1

	def isInside(point: Point, polygon: Polygon): Boolean = {
		val ray = Line(point, point.copy(x = xmax))
		geometry.polygonLines(polygon)
			.count(_ intersect ray) % 2 != 0
	}
}
