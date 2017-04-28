package compgeom.geometry

case class Point(x: Double = 0, y: Double = 0) extends Ordered[Point] {

	def toTheLeftOf(line: Line): Boolean =
		crossProduct(line) < 0

	def toTheRightOf(line: Line): Boolean =
		crossProduct(line) > 0

	def onThe(line: Line): Boolean =
		crossProduct(line) == 0

	private def crossProduct(line: Line) = {
		val Line(a, b) = line

		(x - a.x) * (b.y - a.y) -
			(y - a.y) * (b.x - a.x)
	}

	def distanceSquaredTo(other: Point): Double = {
		val dx = x - other.x
		val dy = y - other.y
		dx * dx + dy * dy
	}

	def distanceTo(other: Point): Double =
		math.sqrt(distanceSquaredTo(other))

	def distanceTo(line: Line): Double = {
		import math.{pow, sqrt, abs}

		val Line(a, b) = line
		val normal = sqrt(pow(b.x - a.x, 2) + pow(b.y - a.y, 2))
		abs(crossProduct(line)) / normal
	}

	def collinear(a: Point, b: Point): Boolean =
		signedArea(a, b) == 0

	def cw(a: Point, b: Point): Boolean =
		signedArea(a, b) < 0

	def ccw(a: Point, b: Point): Boolean =
		signedArea(a, b) > 0

	private def signedArea(a: Point, b: Point): Double =
		(a.x - x) * (b.y - y) -
			(a.y - y) * (b.x - x)

	override def compare(other: Point): Int =
		if (y < other.y) -1 else if (y > other.y) 1
		else if (x < other.x) -1 else if (x > other.x) 1
		else 0

	override def toString: String = s"($x, $y)"
}
