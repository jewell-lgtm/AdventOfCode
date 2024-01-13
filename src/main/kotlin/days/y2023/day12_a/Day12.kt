package days.y2023.day12_a


import util.InputReader
import util.timeIt

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day12(val input: PuzzleInput) {

    fun partOne(): Long {
        return input.sumOf { line ->
            val config = line.toConfiguration()
            getCount(config.str, config.widths)
        }
    }

    fun partTwo(): Long {
        val bigInput = input.map { line -> line.toConfiguration().bigify(5) }
        return bigInput.sumOf { line ->
            getCount(line.str, line.widths)
        }
    }


    private val cachedCounts = mutableMapOf<Pair<String, List<Int>>, Long>()
    private fun getCount(remaining: String, widths: List<Int>): Long =
        cachedCounts.getOrPut(remaining to widths) {
            when {
                remaining.isEmpty() -> if (widths.isEmpty()) 1L else 0L
                widths.isEmpty() -> if (remaining.contains('#')) 0L else 1L
                else -> {
                    val block = widths.first()
                    var result = 0L

                    result +=
                        if (remaining.couldBeFunctional()) getCount(remaining.drop(1), widths)
                        else 0L

                    result += if (remaining.couldBeBroken()) {
                        if (remaining.fitsBlock(block)) getCount(remaining.drop(block + 1), widths.drop(1))
                        else 0L
                    } else 0L

                    return@getOrPut result
                }
            }
        }

    private fun String.fitsBlock(block: Int) = length >= block &&
            !(substring(0, block).contains('.')) &&
            (length == block || (this)[block] in setOf('.', '?'))

    private fun String.couldBeBroken() = first() in setOf('#', '?')

    private fun String.couldBeFunctional() = first() in setOf('.', '?')
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


fun Configuration.bigify(n: Int) = copy(
    str = List(n) { this.str }.joinToString("?"),
    widths = List(n) { this.widths }.flatten()
)


fun main() {
    timeIt {
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
}

