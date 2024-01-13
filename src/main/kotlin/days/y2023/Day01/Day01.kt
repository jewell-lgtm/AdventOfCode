package days.y2023.Day01

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day00(val input: PuzzleInput) {
    fun partOne(): Int {
        input.forEach { println(it) }
        return -1
    }
}

fun main() {
    val year = 2023
    val day = 1

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day00(input).partOne()

    println("Example 1: ${partOne(exampleInput)}")
}

