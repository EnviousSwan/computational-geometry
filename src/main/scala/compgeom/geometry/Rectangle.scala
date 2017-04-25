package compgeom.geometry

case class Rectangle(xmin: Double, ymin: Double,
                     xmax: Double, ymax: Double) {

	def contains(point: Point): Boolean =
		point.x > xmin && point.x < xmax &&
			point.y > ymin && point.y < ymax

	def intersects(other: Rectangle): Boolean =
		xmax >= other.xmin && ymax >= other.ymin &&
			xmin <= other.xmax && ymin <= other.ymax

	def distanceSquaredTo(point: Point): Double = {
		val dx = {
			if (point.x < xmin) point.x - xmin
			else if (point.x > xmax) point.x - xmax
			else 0.0
		}
		val dy = {
			if (point.y < ymin) point.y - ymin
			else if (point.y > ymax) point.y - xmax
			else 0.0
		}

		dx * dx + dy * dy
	}

	override def toString: String = s"[$xmin, $ymin] x [$xmax, $ymax]"
}
