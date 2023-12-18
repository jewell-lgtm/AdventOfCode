package days.y2023.day18

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day18(val input: PuzzleInput) {
    val instructions = input.toInstructions()
    fun partOne(): Int {
        val loop = instructions.drawLoop()
        val minX = loop.minOf { it.x }
        val maxX = loop.maxOf { it.x }
        val minY = loop.minOf { it.y }
        val maxY = loop.maxOf { it.y }
        var result = 0
        val str =
            (minY..maxY).joinToString("\n") { y ->
                (minX..maxX).joinToString("") { x ->
                    if (x == 0 && y == 0) {
                        result += 1
                        "O"
                    } else if (Position(x, y) in loop) {
                        result += 1
                        "#"
                    } else {
                        "."
                    }
                }
            }

        println(str)
        return result
    }


}


private fun List<DigInstruction>.drawLoop(): List<Position> {
    val positions = mutableListOf(Position(0, 0))
    var i = 0
    for (instruction in this) {
        i++
        val lastPosition = positions.last()
        val newPositions = lastPosition.move(instruction.direction, instruction.distance)
        positions.addAll(newPositions)
    }
    println(i)
    return positions
}



data class Position(val x: Int, val y: Int) {
    fun move(direction: Direction, distance: Int): List<Position> {
        return (1..distance).map { d ->
            when (direction) {
                Direction.Up -> Position(x = x, y = y - d)
                Direction.Down -> Position(x = x, y = y + d)
                Direction.Left -> Position(x = x - d, y = y)
                Direction.Right -> Position(x = x + d, y = y)
            }
        }
    }

}

enum class Direction {
    Up, Down, Left, Right
}

data class DigInstruction(val direction: Direction, val distance: Int) {
    companion object {
        fun fromString(input: String): DigInstruction {
            val direction = when (input[0]) {
                'U' -> Direction.Up
                'D' -> Direction.Down
                'L' -> Direction.Left
                'R' -> Direction.Right
                else -> throw IllegalArgumentException("Invalid direction: ${input[0]}")
            }
            val distance = input.substring(2, 3).toInt()
            return DigInstruction(direction, distance)
        }
    }
}

fun PuzzleInput.toInstructions(): List<DigInstruction> {
    return this.map { DigInstruction.fromString(it) }
}

fun main() {
    val year = 2023
    val day = 18

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day18(input).partOne()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
}

