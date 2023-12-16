package days.y2023.day12

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day12(val input: PuzzleInput) {
    fun partOne(): Int {
        return input.sumOf { line -> line.toConfiguration().arrangements().size }
    }

    fun partTwo(): Int {
        val bigInput = input.map { line -> line.toConfiguration().bigify(5) }
        return bigInput.sumOf { line -> line.arrangements().size }
    }
}

data class Configuration(
    val str: String,
    val widths: List<Int>
)

fun PuzzleLine.toConfiguration(): Configuration {
    val (head, blocks) = this.split(" ")
    return Configuration(head.chompDots(), blocks.split(",").map { it.toInt() })
}

fun String.chompDots(): String {
    return this.replace(Regex("\\.+"), ".")
}


fun Configuration.arrangements(): Set<Configuration> {
    if (!potentiallyValid()) {
        return emptySet()
    }
    if (!this.str.contains('?')) {
        return setOf(this)
    }
    val left = copy(str = str.replaceFirst('?', '.')).arrangements()
    val right = copy(str = str.replaceFirst('?', '#')).arrangements()
    return left + right
}


fun Configuration.potentiallyValid(): Boolean {
    val islands = this.str.split('.').filter { it.isNotEmpty() }
    val actual = islands.mapNotNull { it.takeIf { !it.contains('?') }?.length }
    if (!this.str.contains('?')) return actual == widths
    if (actual.size > widths.size) return false
    // the elements in actual must appear in the right order in expected
    var i = 0
    var j = 0
    while (i < actual.size && j < widths.size) {
        val a = actual[i]
        val b = widths[j]
        if (a == b) {
            i++
            j++
        } else {
            j++
        }
    }
    return i == actual.size
}

fun Configuration.bigify(n: Int) = copy(
    str = List(n) { this.str }.joinToString("?"),
    widths = List(n) { this.widths }.flatten()
)


fun main() {
    val year = 2023
    val day = 12

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day12(input).partOne()
    fun partTwo(input: PuzzleInput) = Day12(input).partTwo()


    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}

