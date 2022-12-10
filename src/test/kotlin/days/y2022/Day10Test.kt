@file:Suppress("UNCHECKED_CAST")

package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day10 : Day(2022, 10) {
    override fun partOne(input: String): Any {
        val state = execute(parseInput(input))
        return listOf(
            state.registerAt[19] * 20,
            state.registerAt[59] * 60,
            state.registerAt[99] * 100,
            state.registerAt[139] * 140,
            state.registerAt[179] * 180,
            state.registerAt[219] * 220,
        )
    }

    override fun partTwo(input: String): Any {
        val result: MutableList<MutableList<Char>> = mutableListOf()
        val programState = execute(parseInput(input))
        val rowWidth = 40
        programState.registerAt.dropLast(1).forEachIndexed { index, register ->
            val col = index % rowWidth
            val row = index / rowWidth
            if (result.size <= row) {
                result.add(MutableList(rowWidth) { ' ' })
                print("\n")
            }
            val drawChar = col.drawChar(register)
            result[row][col] = drawChar
            print(drawChar)
        }
        print("\n")

        return result.joinToString("\n") { it.joinToString("") }
    }

    private fun Int.drawChar(register: Int): Char =
        if ((register - 1 .. register + 1).contains(this)) '#'
        else '.'

    fun parseInput(input: String): List<Instruction> = input.trim().lines().map { Instruction.from(it) }

    fun execute(instructions: List<Instruction>): ProgramState =
        instructions.fold(ProgramState()) { state, instruction ->
            state.apply {
                instruction.execute(this)
            }
        }.apply {
            this.registerAt.add(this.register)
        }


    class ProgramState(
        val registerAt: MutableList<Int> = mutableListOf(),
        var clockCycles: Int = 0,
        var register: Int = 1
    ) {
        override fun toString(): String {
            return "ProgramState(result=$registerAt, clockCycles=$clockCycles, register=$register)"
        }
    }

    abstract class Instruction {

        abstract fun execute(programState: ProgramState)

        companion object {
            fun from(string: String) = string.trim().split(" ").let {
                when (it[0]) {
                    "noop" -> Noop()
                    "addx" -> AddX(it[1].toInt())
                    else -> throw IllegalArgumentException("Unknown instruction: $string")
                }
            }
        }
    }

    class Noop : Instruction() {
        override fun toString() = "NoopInstruction()"

        override fun execute(programState: ProgramState) {
            programState.registerAt.add(programState.register)
            programState.clockCycles++
        }
    }

    class AddX(val amount: Int) : Instruction() {
        override fun toString() = "AddXInstruction(amount=$amount)"

        override fun execute(programState: ProgramState) {
            programState.registerAt.add(programState.register)
            programState.clockCycles++

            programState.registerAt.add(programState.register)
            programState.clockCycles++
            programState.register += this.amount
        }
    }
}

class Day10Test {

    @Test
    fun testMiniExampleOne() {
        val instructions = Day10().parseInput(
            """
                        noop
                        addx 3
                        addx -5
                    """.trimIndent()
        )
        val registers = Day10().execute(
            instructions
        ).registerAt.toList()
        val expected = listOf(
            1,
            1,
            1,
            4,
            4,
            -1
        )
        assertThat(registers, `is`(expected))
    }

    @Test
    fun testExampleOne() {
        val actual = Day10().partOne(
            exampleInput.trimIndent()
        )

        val expected = listOf(
            420,
            1140,
            1800,
            2940,
            2880,
            3960
        )
        assertThat(actual, `is`(expected))
    }

    @Test
    fun testPartOne() {
        assertThat((Day10().partOne() as List<Int>).sum(), `is`(16480))
    }

    @Test
    fun testExampleTwo() {
        val exampleOutput = """
##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....
        """.trimIndent()
        assertThat(
            Day10().partTwo(exampleInput), `is`(exampleOutput)
        )
    }

    @Test
    fun testPartTwo() {
        val expectedOut = """
###..#....####.####.#..#.#....###..###..
#..#.#....#....#....#..#.#....#..#.#..#.
#..#.#....###..###..#..#.#....#..#.###..
###..#....#....#....#..#.#....###..#..#.
#....#....#....#....#..#.#....#....#..#.
#....####.####.#.....##..####.#....###..
        """.trimIndent()
        assertThat(Day10().partTwo(), `is`(expectedOut))
    }

    val exampleInput = """
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
"""
}


