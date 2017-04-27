package compgeom.kdtree.logic

import compgeom.geometry.{Line, Point, Rectangle}
import compgeom.kdtree.logic.KDTree._

import scala.annotation.tailrec

sealed trait KDTree {

	def contains(point: Point): Boolean = {
		@tailrec
		def contains(tree: KDTree, point: Point,
		             level: Level): Boolean = tree match {
			case Empty => false
			case Leaf(p, _) => p == point
			case Node(p, _, left, right) =>
				if (p == point) {
					true
				} else {
					if (point smaller(p, level)) contains(left, point, !level)
					else contains(right, point, !level)
				}
		}

		contains(this, point, VERTICAL)
	}

	def isEmpty: Boolean = this match {
		case Empty => true
		case _ => false
	}

	def size: Int = this match {
		case Empty => 0
		case _: Leaf => 1
		case Node(_, _, left, right) => 1 + left.size + right.size
	}

	def draw(): List[(Line, Level)] = {
		def draw(tree: KDTree, level: Level,
		         acc: List[(Line, Level)]): List[(Line, Level)] = tree match {

			case Empty => List()

			case Leaf(point, rect) =>
				drawNode(point, rect, level) :: acc

			case Node(point, rect, left, right) =>
				draw(right, !level, acc) :::
					draw(left, !level, acc) :::
					drawNode(point, rect, level) :: acc
		}

		def drawNode(p: Point, rect: Rectangle, level: Level) = level match {

			case VERTICAL =>
				val a = Point(p.x, rect.ymin)
				val b = Point(p.x, rect.ymax)
				(Line(a, b), VERTICAL)

			case HORIZONTAL =>
				val a = Point(rect.xmin, p.y)
				val b = Point(rect.xmax, p.y)
				(Line(a, b), HORIZONTAL)
		}

		draw(this, VERTICAL, List())
	}

	def insert(point: Point): KDTree = {

		def insert(tree: KDTree, level: Level,
		           rect: Rectangle): KDTree = tree match {

			case Empty => Leaf(point, rect)

			case Leaf(p, _) if point == p => tree

			case Leaf(p, r) =>
				if (point smaller(p, level)) {
					val section = rectangle(level, p, LEFT, rect)
					Node(p, r, Leaf(point, section), Empty)
				} else {
					val section = rectangle(level, p, RIGHT, rect)
					Node(p, r, Empty, Leaf(point, section))
				}

			case Node(p, _, _, _) if point == p => tree

			case Node(p, r, Empty, right) if point smaller(p, level) =>
				val section = rectangle(level, p, LEFT, rect)
				Node(p, r, Leaf(point, section), right)

			case Node(p, r, left, Empty) if !(point smaller(p, level)) =>
				val section = rectangle(level, p, RIGHT, rect)
				Node(p, r, left, Leaf(point, section))

			case Node(p, r, left @ Leaf(_, lRect), right) if point smaller(p, level) =>
				Node(p, r, insert(left, !level, lRect), right)

			case Node(p, r, left, right @ Leaf(_, rRect)) if !(point smaller(p, level)) =>
				Node(p, r, left, insert(right, !level, rRect))

			case Node(p, r, left @ Node(_, lRect, _, _), right)
				if point smaller(p, level) =>
				Node(p, r, insert(left, !level, lRect), right)

			case Node(p, r, left, right @ Node(_, rRect, _, _))
				if !(point smaller(p, level)) =>
				Node(p, r, left, insert(right, !level, rRect))

			case _ => tree
		}

		insert(this, VERTICAL, UNIT)
	}

	def range(rect: Rectangle): List[Point] = this match {

		case Leaf(p, _) if (rect contains p) &&
			(rect distanceSquaredTo p) == 0 => p :: Nil

		case Node(p, r, left, right) if r intersects rect =>
			val points = (left range rect) ::: (right range rect)
			if ((rect contains p) && rect.distanceSquaredTo(p) == 0)
				p :: points
			else points

		case _ => List.empty[Point]
	}
}

case class Node(p: Point, r: Rectangle, left: KDTree, right: KDTree) extends KDTree

case class Leaf(p: Point, r: Rectangle) extends KDTree

case object Empty extends KDTree

object KDTree {

	type Side = Boolean
	type Level = Boolean

	private val LEFT = true
	private val RIGHT = false
	private val UNIT = Rectangle(0, 0, 1, 1)

	val VERTICAL = true
	val HORIZONTAL = false

	def apply(): KDTree = Empty

	private def rectangle(level: Level, point: Point,
	                      side: Side, rect: Rectangle) = {
		val (xmin, xmax) =
			if (level == VERTICAL)
				if (side == LEFT) (rect.xmin, point.x)
				else (point.x, rect.xmax)
			else (rect.xmin, rect.xmax)

		val (ymin, ymax) =
			if (level == HORIZONTAL)
				if (side == LEFT) (rect.ymin, point.y)
				else (point.y, rect.ymax)
			else (rect.ymin, rect.ymax)

		Rectangle(xmin, ymin, xmax, ymax)
	}

	implicit class KdTreePoint(point: Point) {
		def smaller(other: Point, level: Boolean): Boolean =
			level match {
				case VERTICAL => point.x - other.x < 0
				case HORIZONTAL => point.y - other.y < 0
			}
	}
}
