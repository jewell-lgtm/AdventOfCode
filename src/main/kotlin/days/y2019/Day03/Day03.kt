package days.y2019

import days.Day
import kotlin.math.abs

public class Day03 : Day(2019, 3) {

    override fun partOne(input: String): Any {
        val (wireA, wireB) = input.split("\n").map { it.parseWire() }
        val pointsA = wireA.points()
        val pointsB = wireB.points()
        return pointsA.intersect(pointsB).minOfOrNull { it.manhattanDistance(Point(0, 0)) }
            ?: error("No intersections found")
    }

    override fun partTwo(input: String): Any {
        val (wireA, wireB) = input.split("\n").map { it.parseWire() }
        val pointsAndStepsA = wireA.pointsAndSteps()
        val pointsAndStepsB = wireB.pointsAndSteps()


        return pointsAndStepsA.keys.intersect(pointsAndStepsB.keys).minOfOrNull { point ->
            val distA = pointsAndStepsA[point]!!
            val distB = pointsAndStepsB[point]!!
            distA + distB
        }!!
    }
}


private fun Point.manhattanDistance(point: Point): Int = abs(x - point.x) + abs(y - point.y)

data class Point(val x: Int, val y: Int)

private fun List<WireSegment>.points(): Set<Point> {
    val result = mutableSetOf<Point>()
    var x = 0
    var y = 0
    forEach { segment ->
        repeat(segment.distance) {
            when (segment.direction) {
                Direction.UP -> y++
                Direction.DOWN -> y--
                Direction.LEFT -> x--
                Direction.RIGHT -> x++
            }
            result.add(Point(x, y))
        }
    }
    return result
}

private fun List<WireSegment>.pointsAndSteps(): Map<Point, Int> {
    val result = mutableMapOf<Point, Int>()
    var x = 0
    var y = 0
    var stepsTaken = 0
    forEach { segment ->
        repeat(segment.distance) {
            when (segment.direction) {
                Direction.UP -> y++
                Direction.DOWN -> y--
                Direction.LEFT -> x--
                Direction.RIGHT -> x++
            }
            stepsTaken++
            result[Point(x, y)] = stepsTaken
        }
    }
    return result
}

private fun String.parseWire(): List<WireSegment> = split(",").map {
    val direction = Direction.from(it[0])
    val distance = it.substring(1).toInt()
    WireSegment(direction, distance)
}

data class WireSegment(val direction: Direction, val distance: Int)

enum class Direction(val char: Char) {
    UP('U'),
    DOWN('D'),
    LEFT('L'),
    RIGHT('R');

    companion object {
        fun from(c: Char): Direction = values().first { it.char == c }
    }
}