package days.y2023.day13_a

import util.InputReader
import util.timeIt
import kotlin.math.pow


class Day13(val input: String) {
    private val grids = input.split("\n\n").map { it.lines() }

    fun partOne(): Int {
        return grids.sumOf { grid ->
            grid.findMirror()?.let { it * 100 }
                ?: grid.cols().findMirror()
                ?: error("No mirror found")
        }
    }

    fun partTwo(): Int {
        for (grid in grids) {
            grid.findSmudge()
        }
        return grids.sumOf { grid ->
            grid.findSmudge().let { (row, col) -> row?.let { it * 100 } ?: col ?: error("No smudge found") }
        }
    }


    private fun <E> List<E>.findMirror(): Int? =
        this.indices.firstOrNull { this.reflectsAt(it) }

    private fun <E> List<E>.reflectsAt(index: Int): Boolean {
        if (index == 0) return false
        return this.reflectedPairsFrom(index).all { (left, right) -> left == right }
    }

    private fun <E> List<E>.reflectedPairsFrom(index: Int): Sequence<Pair<E, E>> {
        var i = index
        var j = index - 1
        return generateSequence {
            if (i < this.size && j >= 0) {
                val pair = this[i] to this[j]
                i += 1
                j -= 1
                pair
            } else {
                null
            }
        }
    }

    private fun List<String>.cols(): List<String> =
        this.first().toString().indices
            .map { i ->
                this.map { it[i] }
                    .joinToString("")
            }

    private fun List<String>.findSmudge(): Pair<Int?, Int?> {
        val originalRows = findMirror()
        val originalCols = cols().findMirror()

        for (y in indices) {
            for (x in this[y].indices) {
                val newGrid = flip(y, x)
                val newRows = newGrid.findMirror()
                val newCols = newGrid.cols().findMirror()
                if (newRows != originalRows) return newRows to null
                if (newCols != originalCols) return null to newCols
            }
        }

        error("No smudge found!")
    }

    private fun List<String>.flip(y: Int, x: Int): List<String> {
        val newGrid = this.toMutableList()
        val flipped = if (this[y][x] == '#') "." else "#"
        newGrid[y] = newGrid[y].replaceRange(x, x + 1, flipped)
        return newGrid
    }
}


fun main() {
    timeIt {
        val year = 2023
        val day = 13

        val exampleInput = InputReader.getExample(year, day)
        val puzzleInput = InputReader.getPuzzle(year, day)

        fun partOne(input: String) = Day13(input).partOne()
        fun partTwo(input: String) = Day13(input).partTwo()

        println("Example 1: ${partOne(exampleInput)}")
        println("Part 1: ${partOne(puzzleInput)}")
        println("Example 2: ${partTwo(exampleInput)}")
        println("Part 2: ${partTwo(puzzleInput)}")
    }
}

