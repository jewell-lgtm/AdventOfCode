package days.y2023.day17

import util.InputReader
import java.util.PriorityQueue
import kotlin.math.abs

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day17(val input: PuzzleInput) {
    val grid = input.toGrid()
    fun partOne(): Int {
        val queue =
            makeQueue(
                grid, listOf(
                    GameState(0, Direction.X, grid.start),
                    GameState(0, Direction.Y, grid.start)
                )
            )

        var endState: GameState? = null

        while (queue.isNotEmpty() && endState == null) {
            val currBest = queue.poll()
            if (currBest.position == grid.end) {
                endState = currBest
                continue
            }
            val direction = currBest.nextDirection

            val toVisit = when (direction) {
                Direction.X -> listOf(-3, -2, -1, 1, 2, 3).map { currBest.position.copy(x = currBest.position.x + it) }
                Direction.Y -> listOf(-3, -2, -1, 1, 2, 3).map { currBest.position.copy(y = currBest.position.y + it) }
            }

            val nextStates = toVisit.mapNotNull { nextPosition ->
                if (grid.positionIsValid(nextPosition) && nextPosition !in currBest.visited) {
                    val positionsBetween = currBest.position.positionsBetween(nextPosition)
                    val heatLost = currBest.heatLost + positionsBetween.sumOf { grid.heatLoss[it.y][it.x] }
                    val visited = currBest.visited + positionsBetween
                    GameState(heatLost, direction.other(), nextPosition, visited)
                } else null
            }


            queue.addAll(nextStates)
        }


        requireNotNull(endState)
        return endState.heatLost
    }


}

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

data class Position(val y: Int, val x: Int)

fun Position.distanceTo(other: Position): Int {
    return abs(y - other.y) + abs(x - other.x)
}

data class Grid2d(val heatLoss: List<List<Int>>)

enum class Direction {
    X, Y
}

fun Direction.other(): Direction = when (this) {
    Direction.X -> Direction.Y
    Direction.Y -> Direction.X
}

data class GameState(
    val heatLost: Int,
    val nextDirection: Direction,
    val position: Position,
    val visited: Set<Position> = setOf(position),
)

private fun GameState.heuristic(end: Position, heatLossPerSquare: Float): Float {
//    val heatLost = if (heatLost == 0) 1f else 1f / heatLost.toFloat()
    val distance = if (position.distanceTo(end) == 0) 1f else 1f / position.distanceTo(end).toFloat()
//    return (heatLost + distance) / 2f
    return distance
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

fun makeQueue(grid2d: Grid2d, startingValues: List<GameState> = emptyList()): PriorityQueue<GameState> {
    val end = grid2d.end
    val averageHeatLoss = grid2d.heatLoss.flatten().average()

    val distances: MutableMap<Position, Double> = mutableMapOf()

    fun MutableMap<Position, Double>.distance(position: Position) =
        getOrPut(position) { averageHeatLoss * position.distanceTo(end).toDouble() }

    return PriorityQueue<GameState> { a, b ->
        val aCost = a.heatLost + ( distances.distance(a.position))
        val bCost = b.heatLost + ( distances.distance(b.position))
        aCost.compareTo(bCost)
    }.also { it.addAll(startingValues) }
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

