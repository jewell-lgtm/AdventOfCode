package days.y2023.day13

import util.InputReader


typealias PuzzleInput = String

class Day13(val input: PuzzleInput) {
    val maps = input.toPuzzleMaps()

    fun partOne(): Int {
        return maps.sumOf { it.score() }
    }
}

fun String.toPuzzleMaps(): List<PuzzleMap> {
    val chunks = this.split("\n\n")
    return chunks.map { it.toPuzzleMap() }
}

fun String.toPuzzleMap(): PuzzleMap {
    return PuzzleMap(this.trim().lines().map { it.toList() })
}


data class PuzzleMap(val chars: List<List<Char>>) {
    override fun toString(): String {
        return chars.joinToString("\n") { it.joinToString("") }
    }
}

fun PuzzleMap.score(): Int {
    val yReflections = (1 until chars.first().size).mapNotNull { y ->
        if (reflectAboutY(y) == this) y else null
    }
    val xReflections = (1 until chars.size).mapNotNull { x ->
        if (reflectAboutX(x) == this) x else null
    }

    // To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection;
    // to that, also add 100 multiplied by the number of rows above each horizontal line of reflection.
    val yScore = yReflections.sumOf { it }
    val xScore = xReflections.sumOf { 100 * it }

    return yScore + xScore
}

fun PuzzleMap.reflectAboutY(y: Int): PuzzleMap {
    val newChars = chars.map { line -> line.reflectAbout(y) }
    return PuzzleMap(newChars)
}

fun PuzzleMap.reflectAboutX(x: Int): PuzzleMap {
    val newChars = chars.reflectAbout(x)
    return PuzzleMap(newChars)
}

fun <E> List<E>.reflectAbout(i: Int): List<E> {
    var (head, tail) = this.splitAt(i)
    tail = tail.toMutableList()
    head.reversed().forEachIndexed { at, item ->
        if (at < tail.size) {
            tail[at] = item
        }
    }
    return head + tail
}


private fun <E> List<E>.splitAt(y: Int): Pair<List<E>, List<E>> {
    val before = this.subList(0, y)
    val after = this.subList(y, this.size)
    return Pair(before, after)
}


fun main() {
    val year = 2023
    val day = 13

    val exampleInput: PuzzleInput = InputReader.getExample(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleInput(year, day)

    fun partOne(input: PuzzleInput) = Day13(input).partOne()


    println(exampleInput.toPuzzleMaps()[0])




    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
}

