package compgeom.geometry

case class Point(x: Double, y: Double) extends Ordered[Point] {

	def distanceSquaredTo(other: Point): Double = {
		val dx = x - other.x
		val dy = y - other.y
		dx * dx + dy * dy
	}

	def toTheLeft(line: Line): Boolean =
		slope(line) < 0

	def toTheRight(line: Line): Boolean =
		slope(line) >  0

	def onThe(line: Line): Boolean =
		slope(line) == 0

	private def slope(line: Line) = {
		val Line(a, b) = line

		(x - a.x) * (b.y - a.y) -
			(y - a.y) * (b.x - a.x)
	}

	def distanceTo(other: Point): Double =
		math.sqrt(distanceSquaredTo(other))

	def distanceTo(line: Line): Double = {
		import math.{pow, sqrt, abs}

		val Line(a, b) = line
		val normal = sqrt(pow(b.x - a.x, 2) + pow(b.y - a.y, 2))
		abs(slope(line)) / normal
	}

//	def draw(): Unit = StdDraw.point(x, y)

	override def canEqual(that: Any): Boolean = that.isInstanceOf[Point]

	override def equals(obj: scala.Any): Boolean = obj match {
		case point: Point =>
			point.canEqual(this) && x == point.x && y == point.y
		case _ => false
	}

	override def compare(other: Point): Int =
		if (y < other.y) -1 else if (y > other.y) 1
		else if (x < other.x) -1 else if (x > other.x) 1
		else 0

	override def toString: String = s"($x, $y)"
}
