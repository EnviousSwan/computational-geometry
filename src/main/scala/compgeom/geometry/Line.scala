package compgeom.geometry

case class Line(a: Point, b: Point) {
	def centre: Point =
		Point((a.x + b.x) / 2, (a.y + b.y) / 2)
}
