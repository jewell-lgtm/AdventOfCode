package days.y2019.IntCodeComputer

import util.integers


class IntCodeComputer {
    fun run(inputTape: String): String = run(inputTape.integers(",")).joinToString(",")

    val outputValues = mutableListOf<Int>()
    val output: List<Int> get() = outputValues.toList()

    @Suppress("MemberVisibilityCanBePrivate")
    fun run(inputTape: List<Int>): List<Int> {
        val tape = inputTape.toMutableList()
        var pos = 0
        var ticks = 0
        outputValues.clear()
        while (tape.getOrNull(pos) != null) {
            ticks++
            val instruction = tape[pos]
            val opcode = instruction.opcode
            val (parA, parB, parC) = tape.parModes(pos)
            when (opcode) {
                1 -> {
                    val (a, b, c) = tape.subList(pos + 1, pos + 4)
                    tape.setMode(parC, c, tape.getMode(parA, a) + tape.getMode(parB, b))
                    pos += 4
                }
                2 -> {
                    val (a, b, c) = tape.subList(pos + 1, pos + 4)
                    val valA = tape.getMode(parA, a)
                    val valB = tape.getMode(parB, b)
                    tape.setMode(parC, c, valA * valB)
                    pos += 4
                }
                3 -> {
                    val input = 1
                    val a = tape[pos + 1]
                    tape[a] = input
                    pos += 2
                }
                4 -> {
                    val a = tape[pos + 1]
                    val output = tape.getMode(parA, a)
                    outputValue(output)
                    pos += 2
                }
                99 -> {
                    println("Halting after $ticks ticks")
                    pos = -1
                }
                else -> {
                    throw IllegalArgumentException("Unknown opcode $opcode")
                }
            }
        }


        return tape
    }


    private fun outputValue(output: Int) {
        outputValues.add(output)
        println("Output: $output")
    }



}

private fun MutableList<Int>.setMode(mode: ParameterMode, index: Int, value: Int) {
    when (mode) {
        ParameterMode.POSITION -> this[index] = value
        ParameterMode.IMMEDIATE -> throw IllegalArgumentException("Can't set immediate mode")
    }
}

private fun MutableList<Int>.getMode(mode: ParameterMode, index: Int): Int = when (mode) {
    ParameterMode.POSITION -> this[index]
    ParameterMode.IMMEDIATE -> index
}

enum class ParameterMode {
    POSITION, IMMEDIATE;

    companion object {
        fun tupleFrom(i: Int): Triple<ParameterMode, ParameterMode, ParameterMode> {
            val (a, b, c) = (i / 100).toString().padStart(3, '0').reversed().map { it.toString().toInt() }
            return Triple(a.toParMode(), b.toParMode(), c.toParMode())
        }

        fun from(i: Int): ParameterMode = when (i) {
            0 -> POSITION
            1 -> IMMEDIATE
            else -> throw IllegalArgumentException("Unknown parameter mode $i")
        }
    }
}

private fun Int.toParMode(): ParameterMode = ParameterMode.from(this)

private val Int.opcode: Int
    get() {
        return this % 100
    }

private fun MutableList<Int>.parModes(pos: Int): Triple<ParameterMode, ParameterMode, ParameterMode> =
    ParameterMode.tupleFrom(this[pos])
