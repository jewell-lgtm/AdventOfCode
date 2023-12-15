package days.y2023.day15

import util.InputReader


typealias PuzzleInput = String

class Day15(val input: PuzzleInput) {
    fun partOne(): Int {
        // Determine the ASCII code for the current character of the string.
        // Increase the current value by the ASCII code you just determined.
        // Set the current value to itself multiplied by 17.
        // Set the current value to the remainder of dividing itself by 256.
        val strs = input.split(",").map {
            it to it.fold(0) { hash, c ->
                ((hash + c.toInt()) * 17) % 256
            }
        }
        return strs.sumOf { it.second }
    }

    fun partTwo(): Int {
        val operations = input.split(",").map {
            when {
                it.last() == '-' -> Operation.Minus(
                    it.dropLast(1),
                    it.dropLast(1).fold(0) { hash, c ->
                        ((hash + c.toInt()) * 17) % 256
                    }
                )

                it.contains('=') -> it.split('=').let { (label, int) ->
                    Operation.Eq(
                        label,
                        label.fold(0) { hash, c ->
                            ((hash + c.toInt()) * 17) % 256
                        },
                        int.toInt()
                    )
                }

                else -> error("Unknown operation: $it")
            }

        }
        val boxes = mutableMapOf<Int, MutableList<Lens>>()
        for (op in operations) {
            when (op) {
                is Operation.Minus -> {
                    val box = boxes[op.hash] ?: mutableListOf()
                    box.removeAll { it.label == op.label }
                    boxes[op.hash] = box
                }

                is Operation.Eq -> {
                    val box = boxes[op.hash] ?: mutableListOf()
                    val existingIndex = box.indexOfFirst { it.label == op.label }.takeIf { it != -1 }
                    if (existingIndex == null) {
                        box.add(Lens(op.label, op.focalLength))
                    } else {
                        box[existingIndex] = Lens(op.label, op.focalLength)
                    }
                    boxes[op.hash] = box
                }
            }
        }
        return (0..255).map { boxes[it] ?: mutableListOf() }.mapIndexed { i, lenses ->
            val box = i + 1
            (lenses.mapIndexed { j, lens ->
                val slot = j + 1
                val result = box * slot * lens.focalLength
                result
            }.sum())
        }.sum()
    }

    data class Lens(
        val label: String,
        val focalLength: Int
    )


    sealed class Operation {
        abstract val label: String
        abstract val hash: Int

        data class Minus(
            override val label: String,
            override val hash: Int,
        ) : Operation()

        data class Eq(
            override val label: String,
            override val hash: Int,
            val focalLength: Int
        ) : Operation()
    }
}

fun main() {
    val year = 2023
    val day = 15

    val exampleInput: PuzzleInput = InputReader.getExample(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleInput(year, day)

    fun partOne(input: PuzzleInput) = Day15(input).partOne()
    fun partTwo(input: PuzzleInput) = Day15(input).partTwo()

    println("HASH: ${partOne("HASH")}")
    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}

