package compgeom.convexhull.simplepolygon.logic

import compgeom.geometry.{Line, Point}

import scala.annotation.tailrec

object SimplePolygonHull {

	def build(points: List[Point]): List[Point] = {
		val left = points.head
		val right = points.maxBy(_.x)
		val (upper, lower) = points.span(_ != right)

		left :: scan(upper :+ lower.head, right) :::
			right :: scan(lower :+ upper.head, left)
	}

	private def scan(points: List[Point], end: Point): List[Point] = {
		@tailrec
		def loop(hull: List[Point], prev: List[Point],
		         points: List[Point]): List[Point] = (hull, points) match {

			case (_, List(`end`)) => hull

			case (qi :: qi1 :: _, u :: v :: ps)
				if qi1.cw(qi, v) && prev.head.cw(qi, v) && !end.ccw(qi, v) =>
					loop(v :: hull, u :: prev, v :: ps)

			case (qi :: _ , _ :: v :: ps) if v toTheLeftOf Line(end, qi) =>
				val (toTheLeft, toTheRight) = (v :: ps).span(_ toTheLeftOf Line(end, qi))
				loop(hull, prev, toTheLeft.last :: toTheRight)

			case (qi :: qi1 :: _, _ :: v :: ps) if v toTheLeftOf Line(qi, qi1) =>
				val (before, after) = (v :: ps).span(_ toTheLeftOf Line(qi, qi1))
				loop(hull, prev, before.last :: after)

			case (qi :: qi1 :: q, _ :: v :: _) if qi1.ccw(qi, v) =>
					loop(qi1 :: q, prev.tail, points)

			case (_, u :: v :: ps) =>
				loop(v :: hull, u :: prev, ps)

			case _ => hull
		}

		val p1 = points.head

		val eps = if (p1.x < end.x) -.01 else .01
		val q0 = p1.copy(y = p1.y + eps)

		loop(List(p1, q0), List(q0), points)
			.reverse.tail.init.tail
	}
}
