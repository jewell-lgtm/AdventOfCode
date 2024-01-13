package days.y2023.day11_a

import util.InputReader
import util.timeIt
import kotlin.math.abs

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>


class Day11(val input: PuzzleInput) {
    private val galaxyPositions = input.galaxyPositions()
    private val galaxyYs = galaxyPositions.map { it.y }.toSet()
    private val galaxyXs = galaxyPositions.map { it.x }.toSet()
    private val emptyYs = input.indices.toSet() - galaxyYs
    private val emptyXs = input[0].indices.toSet() - galaxyXs


    fun partOne(): ULong {
        return galaxyPositions.pairwise().sumOf { (a, b) -> a.nonManhattanDistance(b, 2).toULong() }
    }

    fun partTwo(): ULong {
        return galaxyPositions.pairwise().sumOf { (a, b) -> a.nonManhattanDistance(b, 1000000).toULong() }
    }

    private fun List<Int>.dilateSpacetime(factor: Int): Int = size * factor

    private fun Position.nonManhattanDistance(other: Position, universeAge: Int) =
        (nonManhattanX(other, universeAge) + nonManhattanY(other, universeAge))

    private fun Position.emptyColsBetween(other: Position): List<Int> =
        emptyXs.whereIn(xRange(other))

    private fun Position.emptyRowsBetween(other: Position): List<Int> =
        emptyYs.whereIn(yRange(other))

    private fun Position.nonManhattanY(
        other: Position,
        universeAge: Int
    ) = manhattanY(other) + emptyRowsBetween(other).dilateSpacetime(universeAge - 1)

    private fun Position.nonManhattanX(
        other: Position,
        universeAge: Int
    ) = manhattanX(other) + emptyColsBetween(other).dilateSpacetime(universeAge - 1)
}

private data class Position(val y: Int, val x: Int)

private fun PuzzleInput.galaxyPositions(): List<Position> =
    allCharPositionsWhere { char -> char == '#' }

private fun PuzzleInput.allCharPositionsWhere(predicate: (c: Char) -> Boolean): List<Position> =
    this.flatMapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (predicate(c)) Position(y, x) else null } }

private fun Position.manhattanX(other: Position): Int = abs(this.x - other.x)
private fun Position.manhattanY(other: Position): Int = abs(this.y - other.y)

private fun Position.xRange(other: Position): IntRange = if (this.x < other.x) this.x..other.x else other.x..this.x
private fun Position.yRange(other: Position): IntRange = if (this.y < other.y) this.y..other.y else other.y..this.y


private fun Collection<Int>.whereIn(range: IntRange) = this.filter { it in range }

private fun <E> List<E>.fromIndex(i: Int) = subList(i, size)

private fun <E> List<E>.pairwise(): Sequence<Pair<E, E>> =
    asSequence().flatMapIndexed { i, e ->
        fromIndex(i + 1).asSequence().map { j ->
            Pair(e, j)
        }
    }

fun main() {
    timeIt {
        val year = 2023
        val day = 11

        val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
        val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

        fun partOne(input: PuzzleInput) = Day11(input).partOne()
        fun partTwo(input: PuzzleInput) = Day11(input).partTwo()

        println("Example 1: ${partOne(exampleInput)}")
        println("Part 1: ${partOne(puzzleInput)}")
        println("Example 2: ${partTwo(exampleInput)}")
        println("Part 2: ${partTwo(puzzleInput)}")
    }
}

