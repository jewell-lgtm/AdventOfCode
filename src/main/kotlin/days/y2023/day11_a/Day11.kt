package days.y2023.day11_a

import util.InputReader
import kotlin.math.abs

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day11(val input: PuzzleInput) {
    private val galaxyPositions = input.galaxyPositions()
    private val emptyRows = input.mapIndexedNotNull { index, row -> if (row.all('.')) index else null }
    private val emptyCols = input.mapColsIndexedNotNull { index, row -> if (row.all('.')) index else null }


    fun partOne(): Long {
        return galaxyPositions.pairwise().sumOf { (a, b) -> a.nonManhattanDistance(b, 2) }
    }

    fun partTwo(): Long {
        return galaxyPositions.pairwise().sumOf { (a, b) -> a.nonManhattanDistance(b, 1000000) }
    }


    private data class Position(val y: Int, val x: Int)

    private fun PuzzleInput.allCharPositionsWhere(predicate: (c: Char) -> Boolean): List<Position> =
        this.flatMapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (predicate(c)) Position(y, x) else null } }

    private fun PuzzleInput.galaxyPositions(): List<Position> =
        allCharPositionsWhere { it == '#' }

    private fun String.all(c: Char): Boolean = this.all { it == c }

    private fun <T> List<String>.mapColsIndexedNotNull(transform: (index: Int, col: String) -> T?): List<T> {
        return this[0].indices.mapNotNull { index ->
            transform(index, map { it[index] }.joinToString(""))
        }
    }

    private fun Position.manhattanX(other: Position): Int = abs(this.x - other.x)
    private fun Position.manhattanY(other: Position): Int = abs(this.y - other.y)
    private fun Position.xRange(other: Position): IntRange = if (this.x < other.x) this.x..other.x else other.x..this.x
    private fun Position.yRange(other: Position): IntRange = if (this.y < other.y) this.y..other.y else other.y..this.y

    private fun Position.nonManhattanDistance(other: Position, universeAge: Int): Long {
        val includedEmptyCols = emptyCols.whereContainedBy(xRange(other))
        val includedEmptyRows = emptyRows.whereContainedBy(yRange(other))
        val xDistance = manhattanX(other) + includedEmptyCols.dilateSpacetime(universeAge - 1)
        val yDistance = manhattanY(other) + includedEmptyRows.dilateSpacetime(universeAge - 1)
        return (xDistance + yDistance).toLong()
    }

    private fun List<Int>.dilateSpacetime(factor: Int): Int = size * factor

    private fun List<Int>.whereContainedBy(range: IntRange) = this.filter { it in range }

    private fun <E> List<E>.fromIndex(i: Int) = subList(i, size)

    private fun <E> List<E>.pairwise(): Sequence<Pair<E, E>> = asSequence().flatMapIndexed { i, e ->
        fromIndex(i + 1).asSequence().map { j ->
            Pair(e, j)
        }
    }

}

fun main() {
    val year = 2023
    val day = 11

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    exampleInput.forEach { println(it) }

    fun partOne(input: PuzzleInput) = Day11(input).partOne()
    fun partTwo(input: PuzzleInput) = Day11(input).partTwo()

    println("Example 1: ${partOne(exampleInput)}")
    println("Part 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Part 2: ${partTwo(puzzleInput)}")
}

