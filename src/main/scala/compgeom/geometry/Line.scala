package compgeom.geometry

import java.awt.geom.Line2D

case class Line(a: Point, b: Point) {
	def centre: Point =
		Point((a.x + b.x) / 2, (a.y + b.y) / 2)

	def intersect(other: Line): Boolean =
		Line2D.linesIntersect(a.x, a.y, b.x, b.y,
			other.a.x, other.a.y, other.b.x, other.b.y)
}
