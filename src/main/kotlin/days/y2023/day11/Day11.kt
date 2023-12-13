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

    fun nonManhattanDistance(start: Position, end: Position): Int {
        var dist = 0
        var curr = start
        while (curr.x != end.x) {
            val travelCost = if (xIsEmpty(curr.x) )spaceExpansionQuotient else 1
            dist += travelCost
            curr = curr.copy(x = curr.x.unitTo(end.x))
        }

        while (curr.y != end.y) {
            val travelCost = if (yIsEmpty(curr.y) )spaceExpansionQuotient else 1
            dist += travelCost
            curr = curr.copy(y = curr.y.unitTo(end.y))
        }

        return dist
    }

    private fun isGalaxy(point: Position) = point in positions

    fun allPointsBetween(a: Position, b: Position): List<Position> {
        // first go left/right
        val route = mutableListOf(a)
        while (route.last() != b) {
            route.last().x.unitTo(b.x)
                ?.also { nextX -> route.add(route.last().copy(x = nextX)) }
            route.last().y.unitTo(b.y)
                ?.also { nextY -> route.add(route.last().copy(y = nextY)) }
        }
        return route
    }

    private fun yIsEmpty(that: Int): Boolean {
        return positions.none { it.y == that }
    }

    private fun xIsEmpty(that: Int): Boolean {
        return positions.none { it.x == that }
    }

    fun at(y: Int, x: Int): Position =
        positions.firstOrNull() { it.y == y && it.x == x } ?: error("Nothing at $y, $x")
}

private fun Int.unitTo(x: Int) = when {
    this < x -> this + 1
    this > x -> this - 1
    else -> 0
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

fun <E> assertEq(a: E, b: E) {
    if (a != b) {
        error("Expected $a, got $b")
    }
}

