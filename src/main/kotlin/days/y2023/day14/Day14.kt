package days.y2023.day14

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day14(val input: PuzzleInput) {
    private val start = input.toGrid()

    fun partOne(): Int {
        val rows = start.transpose()
        val rolled = rows.roll()
        val cols = rolled.transpose()
        return countLoad(cols)
    }

    fun partTwo(n: Int = 1000000000): Int {
        val cycleParams = detectCycle()
        val position = cycleParams.get(n)
        return countLoad(position)
    }

    data class CyclicPositions(
        val positions: List<PuzzleGrid>,
        val cycleLength: Int,
    ) {
        val headerSection = positions.subList(0, positions.size - cycleLength)
        val cycleSection = positions.subList(positions.size - cycleLength, positions.size)

        fun get(n: Int): PuzzleGrid {
            val positionN = n -1
            return if (positionN < headerSection.size) {
                headerSection[positionN]
            } else {
                val cycleIndex = positionN - headerSection.size
                val positionIndex = cycleIndex % cycleLength
                cycleSection[positionIndex]
            }
        }
    }

    private fun detectCycle(): CyclicPositions {
        var position = start
        val seenOnce = mutableListOf<PuzzleGrid>()
        val seenTwice = mutableListOf<PuzzleGrid>()
        val seenThrice = mutableListOf<PuzzleGrid>()
        while (true) {
            position = cycle(position)
            if (seenOnce.contains(position)) {
                if (!seenTwice.contains(position)) {
                    seenTwice.add(position)
                } else {
                    if (!seenThrice.contains(position)) {
                        seenThrice.add(position)
                    } else {
                        require(seenThrice.size == seenTwice.size) { "P sure these should always be equal" }
                        return CyclicPositions(seenOnce, seenThrice.size)
                    }
                }
            } else {
                seenOnce.add(position)
            }
        }
    }


    private fun percentComplete(i: Int, ofI: Int): String {
        return "%.2f".format(i.toFloat() / ofI.toFloat() * 100)
    }

    var cacheHits = 0
    var cacheMisses = 0
    var seenCache = false
    val memo: MutableMap<PuzzleGrid, PuzzleGrid> = mutableMapOf()
    fun cycle(grid: PuzzleGrid): List<List<Char>> {
        if (memo.contains(grid)) {
            seenCache = true
            cacheHits += 1
            return memo[grid]!!
        } else {
            cacheMisses += 1
        }

        var result = grid

        // this is definitely not the most efficient way to do this
        // roll north = transpose, roll, transpose
//        println("start")
//        println(toString(result))
        result = result.transpose().roll().transpose()
//        println("roll north")
//        println(toString(result))
        // roll west == roll
        result = result.roll()
//        println("roll west")
//        println(toString(result))
        // roll south = reverse, roll north, reverse
        result = result.reversed().transpose().roll().transpose().reversed()
//        println("roll south")
//        println(toString(result))
        result = result.transpose().reversed().transpose().roll().transpose().reversed().transpose()
//        println("roll east")
//        println(toString(result))

        return result.also { memo[grid] = it }
    }


    private fun countLoad(rows: PuzzleGrid): Int {
        val reverseRows = rows.reversed()
        val counts = reverseRows.mapIndexed { i, row -> i + 1 to row.count { it == 'O' } }
        return counts.sumOf { it.first * it.second }
    }

    private fun PuzzleGrid.roll(): PuzzleGrid =
        map { row -> rollRow(row) }

    private fun rollRow(row: List<Char>) =
        mutableListOf<Char>().apply {
            var skippedDots = 0
            row.forEach { c ->
                if (c == '.') {
                    skippedDots++
                } else {
                    if (c != 'O' && skippedDots > 0) {
                        this.addAll(".".repeat(skippedDots).toList())
                        skippedDots = 0
                    }
                    this += c
                }
            }
            if (skippedDots > 0) {
                this.addAll(".".repeat(skippedDots).toList())
            }
            require(this.size == row.size) {
                "Result size ${this.size} != row size ${row.size}"
            }
        }

}


typealias PuzzleGrid = List<List<Char>>

fun PuzzleInput.toGrid(): PuzzleGrid {
    return this.map { it.toList() }
}

fun toString(grid: PuzzleGrid): String {
    return grid.joinToString("\n") { it.joinToString("") }
}


private fun PuzzleGrid.transpose(): PuzzleGrid {
    val rows = this.size
    val cols = this[0].size
    val newGrid = MutableList(cols) { MutableList(rows) { ' ' } }

    for (row in 0 until rows) {
        for (col in 0 until cols) {
            newGrid[col][row] = this[row][col]
        }
    }

    return newGrid
}

fun PuzzleGrid.rotateClockwise(): PuzzleGrid {
    return this.transpose().map { it.reversed() }
}

fun PuzzleGrid.rotateCounterClockwise(): PuzzleGrid {
    return this.reversed().transpose()
}


fun main() {
    val year = 2023
    val day = 14

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day14(input).partOne()
    fun partTwo(input: PuzzleInput) = Day14(input).partTwo()
    fun partTwo(input: PuzzleInput, n: Int) = Day14(input).partTwo(n)


    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")

    println("Example 2: ${partTwo(exampleInput, 1)}")
    println("Example 2: ${partTwo(exampleInput, 2)}")
    println("Example 2: ${partTwo(exampleInput, 3)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}

