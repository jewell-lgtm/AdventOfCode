package days.y2023.day23

import util.InputReader

class Day23(val input: List<String>) {
    private val start = Position(0, input.first().indexOfOnly('.'))
    private val end = Position(input.size - 1, input.last().indexOfOnly('.'))
    private val neighbors = input.flatMapIndexed { y, row ->
        row.indices.map { x ->
            Position(y, x) to input.neighbors(Position(y, x))
        }
    }.toMap()
    private val cells = input.flatMapIndexed { y, row ->
        row.indices.map { x ->
            Position(y, x) to row[x]
        }
    }.toMap()

    fun partOne(): Int = longestPath(true, start, setOf(start))
    fun partTwo(): Int = longestPath(false, start, setOf(start))

    private fun longestPath(slipperyHills: Boolean, from: Position, visited: Set<Position>): Int {
        if (from == end) return visited.size - 1
        val unvisited = from.neighbors().filter { it !in visited }
        if (unvisited.isEmpty()) return -1

        return if (!slipperyHills) {
            unvisited.maxOfOrNull { longestPath(false, it, visited + it) } ?: -1
        } else {
            unvisited.mapNotNull { next ->
                when (cells.getValue(next)) {
                    '.' -> longestPath(true, next, visited + next)
                    '>' -> if (next.right() !in visited) longestPath(
                        true,
                        next.right(),
                        visited + next + next.right()
                    ) else null
                    '<' -> if (next.left() !in visited) longestPath(
                        true,
                        next.left(),
                        visited + next + next.left()
                    ) else null
                    '^' -> if (next.up() !in visited) longestPath(
                        true,
                        next.up(),
                        visited + next + next.up()
                    ) else null
                    'v' -> if (next.down() !in visited) longestPath(
                        true,
                        next.down(),
                        visited + next + next.down()
                    ) else null
                    else -> null
                }
            }.max()
        }
    }

    data class Position(val y: Int, val x: Int)

    private fun String.indexOfOnly(char: Char): Int {
        require(count { it == char } == 1) { "The character $char should only appear once." }
        return indexOf(char)
    }

    private fun Position.neighbors(): Set<Position> = neighbors.getValue(this)
    private fun Position.down() = copy(y = y + 1)
    private fun Position.up() = copy(y = y - 1)
    private fun Position.left() = copy(x = x - 1)
    private fun Position.right() = copy(x = x + 1)

    private fun List<String>.neighbors(position: Position): Set<Position> {
        return listOf(
            position.up(), position.down(), position.left(), position.right()
        ).filter {
            it.y in indices && it.x in this[it.y].indices && this[it.y][it.x] != '#'
        }.toSet()
    }
}

fun main() {
    val year = 2023
    val day = 23

    val exampleInput = InputReader.getExampleLines(year, day)
    val puzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: List<String>) = Day23(input).partOne()
    fun partTwo(input: List<String>) = Day23(input).partTwo()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}
