package days.y2023.day09

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day09(val input: PuzzleInput) {
    val lines = input.map { line -> line.split(" ").map { it.toInt() } }

    fun partOne() = lines.sumOf { findSequence(it) }
    fun partTwo() = lines.sumOf { findSequence2(it) }


    fun findSequence(input: List<Int>): Long {
        val result = mutableListOf(input.toMutableList())
        while (!result.last().isAllZeros()) {
            val current = result.last()
            val diffs = current.windowed(2).map { it[1] - it[0] }
            result.add(diffs.toMutableList())
        }

        result.last().add(0)
        for (y in result.size - 2 downTo 0) {
            val add = result[y].last() + result[y + 1].last()
            result[y].add(add)
        }


        return result[0].last().toLong()
    }

    // part 2 - Where's the tricky part?
    // no fancy shenanigens, just copy paste
    fun findSequence2(input: List<Int>): Long {
        val result = mutableListOf(input.toMutableList())
        while (!result.last().isAllZeros()) {
            val current = result.last()
            val diffs = current.windowed(2).map { it[1] - it[0] }
            result.add(diffs.toMutableList())
        }

        result.last().add(0)
        for (y in result.size - 2 downTo 0) {
            val yF = result[y].first()
            val y1F = result[y + 1].first()
            val add = yF - y1F
            result[y].add(0, add)
        }


        return result[0].first().toLong()
    }
}


fun List<Int>.isAllZeros(): Boolean = this.all { it == 0 }

fun main() {
    val year = 2023
    val day = 9

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day09(input).partOne()
    fun partTwo(input: PuzzleInput) = Day09(input).partTwo()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
//    println("Example 2: ${partTwo(listOf(exampleInput.last()))}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}

