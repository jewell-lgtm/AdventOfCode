package days.y2023.day10

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day10(val input: PuzzleInput) {
    val grid = parseGrid(input)

    fun partOne(): Int {
        val len = findLen()
        return len / 2
    }

    fun partTwo(): Int {
        val pipe = findPipe()
        val pointsToCheck = grid.positions() - pipe.positions()
        val insidePoints = pointsToCheck.filter { grid.pipeContains(pipe, it) }


        return -1
    }



    fun findLen(): Int {
        val visited = findPipe()

        return visited.size
    }

    private fun findPipe(): Set<PipeSection> {
        val start = grid.filterPosition { it == 'S' }.only()

        val startNeighbors = grid.whereNeighbors(start) { section -> section.connectsTo(start) }
            .also { if (it.size != 2) TODO("Loop impossible") }

        val visited = mutableSetOf(start, startNeighbors.first())
        while (true) {
            val curr = visited.last()
            val next = curr.onlyUnvisited(visited) ?: break
            visited.add(next)
        }

        return visited
    }

}

private fun PuzzleGrid.pipeContains(pipe: Set<PipeSection>, position: Position): Boolean {
    val bounds = pipe.positions()
    var isInsideAPipe = false
    var currPosition = position
    while (currPosition.y > 0) {
        if (isInsideAPipe == false) {
            currPosition = currPosition.copy(y = currPosition.y - 1)
        }
    }
}


private fun Collection<PipeSection>.positions(): Set<Position> {
    return this.map { it.position }.toSet()
}

typealias PuzzleGrid = List<List<Char>>

data class Position(val y: Int, val x: Int) {


}

fun parseGrid(input: PuzzleInput): PuzzleGrid {
    return input.map { it.toList() }
}

fun <E> Collection<E>.only(): E {
    if (this.size != 1) throw Exception("Collection has ${this.size} elements")
    return this.first()
}

fun <E> Collection<E>.onlyOrNone(): E? {
    if (this.size > 1) throw Exception("Collection has ${this.size} elements")
    return this.firstOrNull()
}

fun PuzzleGrid.filterPosition(predecate: (Char) -> Boolean): List<PipeSection> {
    return this.mapIndexed { y, row ->
        row.mapIndexed { x, cell ->
            if (predecate(cell)) PipeSection(this, Position(y = y, x)) else null
        }.filterNotNull()
    }.flatten()
}

fun PuzzleGrid.whereNeighbors(start: PipeSection, predicate: (PipeSection) -> Boolean) =
    this.neighbors(start.position).filter { predicate(it) }


fun PuzzleGrid.neighbors(position: Position): Set<PipeSection> {
    return listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).map { (dy, dx) -> Position(y = position.y + dy, position.x + dx) }
        .filter { it.y >= 0 && it.x >= 0 && it.y < this.size && it.x < this[0].size }
        .map { PipeSection(this, it) }
        .toSet()
}

fun PuzzleGrid.positions(): Set<Position> {
    return this.mapIndexed { y, row ->
        row.mapIndexed { x, cell ->
            Position(y = y, x = x)
        }
    }.flatten().toSet()
}

data class PipeSection(val grid: PuzzleGrid, val position: Position) {
    val char = grid[position.y][position.x]
    val y = position.y
    val x = position.x

    override fun toString(): String {
        return "PipeSection($position, $char)"
    }

    fun connectsTo(other: PipeSection): Boolean {
        return connections().contains(other)
    }

    fun connections(): Set<PipeSection> = when (char) {
        '|' -> listOf(north(), south())
        '-' -> listOf(east(), west())
        'L' -> listOf(north(), east())
        'J' -> listOf(north(), west())
        '7' -> listOf(south(), west())
        'F' -> listOf(south(), east())
        '.' -> emptyList()
        'S' -> emptyList()
        else -> TODO("$char")
    }.filterValid().toSet()

    fun north(): PipeSection {
        return PipeSection(grid, Position(y - 1, x))
    }

    fun south(): PipeSection {
        return PipeSection(grid, Position(y + 1, x))
    }

    fun east(): PipeSection {
        return PipeSection(grid, Position(y, x + 1))
    }

    fun west(): PipeSection {
        return PipeSection(grid, Position(y, x - 1))
    }


    fun List<PipeSection>.filterValid(): List<PipeSection> {
        return this.filter { it.y >= 0 && it.x >= 0 && it.y < grid.size && it.x < grid[0].size }
    }
}

private fun PipeSection.onlyUnvisited(
    visited: Collection<PipeSection>
) = connections().filter { !visited.contains(it) }.onlyOrNone()


private fun expandPipe(pipe: Set<PipeSection>): Set<PipeSection> =
    // transforms the positions, so they are 3x bigger
    pipe.map { it.copy(position = it.position * 3) }.toSet()


private operator fun Position.times(i: Int): Position {
    return copy(y = y * i, x = x * i)
}


fun main() {
    val year = 2023
    val day = 10

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day10(input).partOne()
    fun partTwo(input: PuzzleInput) = Day10(input).partTwo()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}

