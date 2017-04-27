package compgeom.convexhull.quickhull.logic

import compgeom.geometry.{Line, Point}

object QuickHull {

	def build(points: List[Point]): (List[Point], List[Line]) = {

		val left = points.minBy(_.x)
		val right = points.maxBy(_.x)
		val middle = Line(left, right)

		val toTheLeft = points
			.filterNot(p => p == left || p == right)
			.filter(_ toTheLeftOf middle)

		val toTheRight = points
			.filterNot(p => p == left || p == right)
			.filter(_ toTheRightOf middle)

		val (upperPoints, upperLines) = split(toTheLeft, left, right)
		val (lowerPoints, lowerLines) = split(toTheRight, right, left)

		val convexHull = left :: upperPoints ::: right :: lowerPoints
		val lines = Line(left, right) :: upperLines ::: lowerLines

		(convexHull, lines)
	}

	private def split(points: List[Point], start: Point, end: Point) = {

		def loop(points: List[Point], start: Point, end: Point,
		         acc: List[Line]): (List[Point], List[Line]) = points match {

			case empty @ List() => (empty, acc)

			case single @ List(point) =>
				val left = Line(start, point)
				val right = Line(point, end)
				(single, acc ::: List(left, right))

			case _: List[Point] =>
				val max = points.maxBy(_ distanceTo Line(start, end))
				val normal = Line(Line(start, end).centre, max)

				val left = Line(start, max)
				val right = Line(max, end)

				val toTheLeft = points.filterNot(_ == max)
					.filter(_ toTheLeftOf Line(start, max))

				val toTheRight = points.filterNot(_ == max)
					.filter(_ toTheLeftOf Line(max, end))

				val (leftPoints, leftLines) = loop(toTheLeft, start, max, acc)
				val (rightPoints, rightLines) = loop(toTheRight, max, end, acc)

				(leftPoints ::: max :: rightPoints,
					normal :: left :: right :: leftLines ::: rightLines)
		}

		loop(points, start, end, List())
	}
}
