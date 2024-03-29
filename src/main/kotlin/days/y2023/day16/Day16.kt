package days.y2023.day16

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day16(val input: PuzzleInput) {
    val maze = Maze(Grid2d.from(input))

    fun partOne(): Int {
        val start = Beam(Position(-1, 0), Direction.RIGHT)
        return energyFor(mutableListOf(start))
    }

    fun partTwo(): Int {
        val starts = listOf(
            input[0].indices.map { x -> Beam(Position(x, -1), Direction.DOWN) },
            input[0].indices.map { x -> Beam(Position(x, input.size), Direction.UP) },
            input.indices.map { y -> Beam(Position(-1, y), Direction.RIGHT) },
            input.indices.map { y -> Beam(Position(input[0].length, y), Direction.LEFT) }
        ).flatten()

        return starts.maxOf { energyFor(mutableListOf(it)) }
    }

    private fun energyFor(startQueue: List<Beam>): Int {
        val energised = mutableSetOf<Position>()
        val queue = startQueue.toMutableList()
        val alreadyTravelled = mutableSetOf<Pair<Position, Direction>>()

        while (queue.isNotEmpty()) {
            val beam = queue.removeFirst()
            val next = maze.advance(alreadyTravelled, energised, beam)
            queue.addAll(next)
        }

        return energised.size
    }
}


data class Grid2d(val chars: List<List<Char>>) {
    fun contains(position: Position): Boolean {
        return position.y in chars.indices && position.x in chars[position.y].indices
    }

    companion object {
        fun from(lines: List<String>): Grid2d {
            return Grid2d(lines.map { it.toList() })
        }
    }
}

data class Position(val x: Int, val y: Int)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Beam(val position: Position, val direction: Direction)


data class Maze(val grid: Grid2d)

fun Maze.advance(
    alreadyTravelled: MutableSet<Pair<Position, Direction>>,
    energized: MutableSet<Position>,
    beam: Beam
): List<Beam> {
    if (alreadyTravelled.contains(beam.position to beam.direction)) return emptyList()
    alreadyTravelled.add(beam.position to beam.direction)
    val nextBeam = beam.advance()
    if (!grid.contains(nextBeam.position)) return emptyList()
    energized.add(nextBeam.position)
    val nextPosition = grid[nextBeam.position]
    val deflected = getDeflected(nextPosition, nextBeam)

    return deflected
}

private fun getDeflected(square: Char, beam: Beam) =
    when (square) {
        '.' -> listOf(beam)
        '|' -> when (beam.direction) {
            Direction.UP, Direction.DOWN -> listOf(beam)
            Direction.LEFT, Direction.RIGHT -> listOf(
                beam.copy(direction = Direction.UP),
                beam.copy(direction = Direction.DOWN)
            )
        }

        '-' -> when (beam.direction) {
            Direction.UP, Direction.DOWN -> listOf(
                beam.copy(direction = Direction.LEFT),
                beam.copy(direction = Direction.RIGHT)
            )
            Direction.LEFT, Direction.RIGHT -> listOf(beam)
        }

        '/' -> when (beam.direction) {
            Direction.UP -> listOf(beam.copy(direction = Direction.RIGHT))
            Direction.DOWN -> listOf(beam.copy(direction = Direction.LEFT))
            Direction.LEFT -> listOf(beam.copy(direction = Direction.DOWN))
            Direction.RIGHT -> listOf(beam.copy(direction = Direction.UP))
        }

        '\\' -> when (beam.direction) {
            Direction.UP -> listOf(beam.copy(direction = Direction.LEFT))
            Direction.DOWN -> listOf(beam.copy(direction = Direction.RIGHT))
            Direction.LEFT -> listOf(beam.copy(direction = Direction.UP))
            Direction.RIGHT -> listOf(beam.copy(direction = Direction.DOWN))
        }

        else -> TODO("$square")
    }

fun Beam.advance() = when (direction) {
    Direction.UP -> Beam(Position(position.x, position.y - 1), direction)
    Direction.DOWN -> Beam(Position(position.x, position.y + 1), direction)
    Direction.LEFT -> Beam(Position(position.x - 1, position.y), direction)
    Direction.RIGHT -> Beam(Position(position.x + 1, position.y), direction)
}

private operator fun Grid2d.get(position: Position): Char {
    return chars[position.y][position.x]
}


fun main() {
    val year = 2023
    val day = 16

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day16(input).partOne()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
    println("Example 2: ${Day16(exampleInput).partTwo()}")
    println("Puzzle 2: ${Day16(puzzleInput).partTwo()}")
}

