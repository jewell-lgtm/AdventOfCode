package days.y2023.day21

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day21(val input: PuzzleInput) {
    data class Position(
        val y: Int,
        val x: Int,
    )

    operator fun PuzzleInput.get(at: Position): Char {
        return this[at.y][at.x]
    }


    fun partOne(n: Int): Int {
        val positions = input.positions()
        val s = positions.first { input[it] == 'S' }
        var lastGeneration = mutableSetOf(s)
        println(printable(input, lastGeneration))
        for (i in 1..n) {
            val nextGeneration = input.notRocks().filter { position ->
                input.neighborsAt(position).intersect(lastGeneration).isNotEmpty()
            }
//            println(printable(input, nextGeneration))
            lastGeneration = nextGeneration.toMutableSet()
        }
        return lastGeneration.size
    }

    private fun printable(input: PuzzleInput, generation: Collection<Position>): String {
        val genSet = generation.toSet()
        return input.mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                when {
                    input.rocks().contains(Position(x, y)) -> '#'
                    genSet.contains(Position(x, y)) -> 'O'
                    else -> c
                }
            }.joinToString("")
        }.joinToString("\n")
    }


}

private fun Day21.Position.neighbors(w: Int, h: Int): Set<Day21.Position> {
    return setOf(
        Day21.Position(x - 1, y),
        Day21.Position(x + 1, y),
        Day21.Position(x, y - 1),
        Day21.Position(x, y + 1)
    ).filter { it.x in 0 until w && it.y in 0 until h }.toSet()
}

val rocksCache = mutableMapOf<PuzzleInput, Set<Day21.Position>>()
private fun PuzzleInput.rocks(): Set<Day21.Position> {
    return rocksCache.getOrPut(this) {
        positions().filter { this[it.y][it.x] == '#' }.toSet()
    }
}

val notRocksCache = mutableMapOf<String, Set<Day21.Position>>()
private fun PuzzleInput.notRocks(): Set<Day21.Position> {
    return notRocksCache.getOrPut(this.joinToString("")) {
        positions().toSet() - rocks().toSet()
    }
}

val neighborsCache = mutableMapOf<String, Map<Day21.Position, Set<Day21.Position>>>()
private fun PuzzleInput.neighbors(): Map<Day21.Position, Set<Day21.Position>> {
    return neighborsCache.getOrPut(this.joinToString("")) {
        positions().associateWith { position ->
            position.neighbors(w = width(), h = height()) - rocks()
        }
    }
}

private fun PuzzleInput.width(): Int {
    return this[0].length
}

private fun PuzzleInput.height(): Int {
    return this.size
}

val positionsCache = mutableMapOf<String, Collection<Day21.Position>>()
private fun PuzzleInput.positions(): Collection<Day21.Position> {
    return positionsCache.getOrPut(this.joinToString("")) {
        flatMapIndexed { y, row ->
            row.mapIndexed { x, _ ->
                (Day21.Position(x, y))
            }
        }
    }
}

private fun PuzzleInput.neighborsAt(position: Day21.Position): Set<Day21.Position> {
    return neighbors()[position]!!
}


fun main() {
    val year = 2023
    val day = 21

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)

    println(exampleInput)

    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput, n: Int = 64) = Day21(input).partOne(n)

//    println("Example 1: ${partOne(exampleInput, 6)}")
    print("Part 1: ${partOne(puzzleInput, 65)}")
}

