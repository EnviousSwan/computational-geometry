package compgeom

package object geometry {
	type Polygon = List[Point]

	def polygonLines(polygon: Polygon): List[Line] = {
		val vertices = polygon.sliding(2)

		for (List(a, b) <- vertices)
			yield Line(a, b)

		vertices.map{ case List(a, b) => Line(a, b) }.toList :+
			Line(polygon.head, polygon.last)
	}
}
