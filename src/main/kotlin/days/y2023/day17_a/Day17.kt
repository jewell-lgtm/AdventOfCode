package days.y2023.day17_a


import util.InputReader
import kotlin.math.abs

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day17(val input: PuzzleInput) {
    val grid = input.toGrid()
    fun partOne(): Int {
        return minOf(
            grid.bestDistanceTo(grid.end, Direction.X),
            grid.bestDistanceTo(grid.end, Direction.Y)
        )
    }


}

private operator fun Grid2d.get(at: Position): Int {
    return this.heatLoss[at.y][at.x]
}

private fun Position.compareTo(other: Position) =
    this.distanceTo(Position(0, 0))
        .compareTo(
            other.distanceTo(Position(0, 0))
        )

// up to and including
private fun Position.positionsBetween(position: Position): Set<Position> {
    val result = mutableSetOf<Position>()
    require(position.y == this.y || position.x == this.x)

    for (y in minOf(position.y, y)..maxOf(position.y, y)) {
        result.add(Position(y, position.x))
    }
    for (x in minOf(position.x, x)..maxOf(position.x, x)) {
        result.add(Position(this.y, x))
    }

    return result - this
}

private fun Grid2d.positionIsValid(position: Position): Boolean {
    return position.y in heatLoss.indices && position.x in heatLoss[position.y].indices
}

data class Position(val y: Int, val x: Int) {
    fun neighbors(grid: Grid2d): Set<Position> {
        return setOf(
            Position(y - 1, x),
            Position(y + 1, x),
            Position(y, x - 1),
            Position(y, x + 1)
        ).filter { grid.positionIsValid(it) }.toSet()
    }
}

fun Position.distanceTo(other: Position): Int {
    return abs(y - other.y) + abs(x - other.x)
}

data class Grid2d(val heatLoss: List<List<Int>>) {
    fun positions() = heatLoss.indices.flatMap { y ->
        heatLoss[y].indices.map { x ->
            Position(y = y, x = x)
        }
    }

    val bestDistances =
        mapOf(
            Direction.X to mutableMapOf(Position(0, 0) to 0),
            Direction.Y to mutableMapOf(Position(0, 0) to 0)
        )

    fun bestDistanceTo(point: Position, fromDirection: Direction): Int {
        if (bestDistances[fromDirection]!!.containsKey(point)) {
            return bestDistances[fromDirection]!![point]!!
        }
        val possiblePreviousPositions = listOf(-3, -2, -1, 1, 2, 3).map { offset ->
            when (fromDirection) {
                Direction.X -> Position(point.y, point.x + offset)
                Direction.Y -> Position(point.y + offset, point.x)
            }
        }.filter { positionIsValid(it) }

        val distance =
            possiblePreviousPositions.minOf { bestDistanceTo(it, fromDirection.other()) + calcCost(it, point) }

        return distance.also { bestDistances[fromDirection]!![point] = it }
    }

    fun calcCost(from: Position, to: Position): Int {
        val positionsBetween = from.positionsBetween(to)
        return positionsBetween.sumOf { this[it] }
    }
}

enum class Direction {
    X, Y
}

fun Direction.other(): Direction = when (this) {
    Direction.X -> Direction.Y
    Direction.Y -> Direction.X
}


val Grid2d.start: Position
    get() = Position(y = 0, x = 0)

val Grid2d.end: Position
    get() = Position(y = heatLoss.size - 1, x = heatLoss[0].size - 1)

val Grid2d.weights: List<List<Int>>
    get() = heatLoss.map { line -> line.map { 10 - it } }

fun Grid2d.goalDistance(a: Position): Int {
    return a.distanceTo(end)
}

fun PuzzleInput.toGrid(): Grid2d {
    return Grid2d(map { line -> line.map { it.toString().toInt() } })
}


fun main() {
    val year = 2023
    val day = 17

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day17(input).partOne()

    println("Example 1: ${partOne(InputReader.getExampleLines(year, day, "example1s"))}")
    println("Example 1: ${partOne(exampleInput)}")
    println("Part 1: ${partOne(puzzleInput)}")
}

