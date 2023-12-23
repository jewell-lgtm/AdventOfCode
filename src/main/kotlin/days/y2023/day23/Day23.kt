package days.y2023.day23

import util.InputReader


class Day23(val input: List<String>) {
    val start = Position(0, input.first().indexOfOnly('.'))
    val end = Position(input.size - 1, input.last().indexOfOnly('.'))
    val neighbors =
        input.flatMapIndexed { y: Int, row: String ->
            row.indices.map { x ->
                Position(y, x) to input.neighbors(
                    Position(y, x)
                )
            }
        }.toMap()
    val cells = input.flatMapIndexed { y: Int, row: String ->
        row.indices.map { x ->
            Position(y, x) to row[x]
        }
    }.toMap()


    fun partOne(): Int {
        return longestPath(true, start, 0, setOf(start))
    }

    fun partTwo(): Int {
        return longestPath(false, start, 0, setOf(start))
    }




    private fun longestPath(slipperyHills: Boolean, from: Position, stepsTaken: Int, visited: Set<Position>): Int {
        if (from == end) return stepsTaken
        val unvisited = from.neighbors().filter { it !in visited }
        if (unvisited.isEmpty()) return -1

        if (!slipperyHills) {
            return unvisited.maxOf { longestPath(false, it, stepsTaken + 1, visited + it) }
        }

        return unvisited.mapNotNull {
            when (cells.getValue(it)) {
                '.' -> if (it !in visited) longestPath(true, it, stepsTaken + 1, visited + it) else null

                '>' -> if (it !in visited && it.right() !in visited) longestPath(
                    true,
                    it.right(),
                    stepsTaken + 2,
                    visited + it + it.right()
                ) else null

                '<' -> if (it !in visited && it.left() !in visited) longestPath(
                    true,
                    it.left(),
                    stepsTaken + 2,
                    visited + it + it.left()
                ) else null

                '^' -> if (it !in visited && it.up() !in visited) longestPath(
                    true,
                    it.up(),
                    stepsTaken + 2,
                    visited + it + it.up()
                ) else null

                'v' -> if (it !in visited && it.down() !in visited) longestPath(
                    true,
                    it.down(),
                    stepsTaken + 2,
                    visited + it + it.down()
                ) else null

                else -> TODO("$it")
            }
        }.max()
    }


    data class Position(val y: Int, val x: Int)


    fun String.indexOfOnly(char: Char): Int {
        require(count { it == char } == 1)
        return indexOf(char)
    }

    fun Position.neighbors(): Set<Position> = neighbors.getValue(this)

    fun Position.down(): Position = copy(y = y + 1)

    fun Position.up(): Position = copy(y = y - 1)

    fun Position.left(): Position = copy(x = x - 1)

    fun Position.right(): Position = copy(x = x + 1)

    private fun List<String>.neighbors(position: Position): Set<Position> {
        return listOf(
            position.copy(y = position.y - 1),
            position.copy(y = position.y + 1),
            position.copy(x = position.x - 1),
            position.copy(x = position.x + 1)
        ).filter {
            it.y in this.indices &&
                    it.x in this[it.y].indices &&
                    this[it.y][it.x] != '#'
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

