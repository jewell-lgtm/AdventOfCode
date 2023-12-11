package days.y2023.day11

import days.y2023.day10.PuzzleGrid
import util.InputReader

typealias PuzzleLine = List<Char>
typealias PuzzleInput = List<PuzzleLine>

class Day11(val input: PuzzleInput) {
    fun partOne(): Int {
        val galaxies = input.toGalaxyMap(2)
        val pairs = galaxies.pairwise()
        println("There are ${pairs.size} pairs")

        return pairs.sumOf { (a, b) -> galaxies.nonManhattanDistance(a, b) }
    }
}

data class Position(val y: Int, val x: Int)

fun <E> List<E>.pairwise(): List<Pair<E, E>> {
    // return every combination of 2 elements from the list
    return this.flatMapIndexed { i, e1 ->
        this.subList(i + 1, this.size).map { e2 -> Pair(e1, e2) }
    }
}

data class GalaxyMap(val positions: Set<Position>, val spaceExpansionQuotient: Int = 1) {
    operator fun get(i: Int): Position {
        return positions.toList()[i]
    }

    fun pairwise(): List<Pair<Position, Position>> {
        return positions.toList().pairwise()
    }

    fun nonManhattanDistance(a: Position, b: Position): Int {
        val xs = (a.x + 1)..b.x
        val ys = (a.y + 1)..b.y

        val emptyYs = ys.filter { y -> this.yIsEmpty(y) }
        val emptyXs = xs.filter { x -> this.xIsEmpty(x) }

        val yCost = ys.sumOf { y -> if (y in emptyYs) spaceExpansionQuotient else 1 }
        val xCost = xs.sumOf { x -> if (x in emptyXs) spaceExpansionQuotient else 1 }


        return yCost + xCost
    }

    private fun yIsEmpty(y: Int): Boolean {
        return positions.none { it.y == y }
    }

    private fun xIsEmpty(x: Int): Boolean {
        return positions.none { it.x == x }
    }

    fun at(y: Int, x: Int): Position {
        val ys = positions.filter { it.y == y }
        val xs = positions.filter { it.x == x }


        return positions.firstOrNull() { it.y == y && it.x == x } ?: throw Exception("Nothing at $y, $x")
    }
}

fun Collection<Position>.toGalaxyMap(quo: Int): GalaxyMap = GalaxyMap(this.toSet(), spaceExpansionQuotient = quo)

fun PuzzleGrid.toGalaxyMap(spaceExpansionQuotient: Int = 1): GalaxyMap =
    this.flatMapIndexed { y: Int, row: List<Char> ->
        row.mapIndexedNotNull { x, char ->
            if (char == '#') Position(y, x) else null
        }
    }.toGalaxyMap(spaceExpansionQuotient)


fun main() {
    val year = 2023
    val day = 11


    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day).map { line -> line.toList() }
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day).map { line -> line.toList() }

    println("${exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(5, 1), map.at(9, 4)) }}")
    println("${exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(0, 3), map.at(8, 7)) }}")
    println("${exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(2, 0), map.at(6, 9)) }}")
    println("${exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(9, 0), map.at(9, 4)) }}")

    assertEq(9, exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(5, 1), map.at(9, 4)) })
    assertEq(15, exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(0, 3), map.at(8, 7)) })
    assertEq(17, exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(2, 0), map.at(6, 9)) })
    assertEq(5, exampleInput.toGalaxyMap(2).let { map -> map.nonManhattanDistance(map.at(9, 0), map.at(9, 4)) })

    fun partOne(input: PuzzleInput) = Day11(input).partOne()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
}

fun <E>assertEq(a: E, b: E) {
    if (a != b) {
        error("Expected $a, got $b")
    }
}

