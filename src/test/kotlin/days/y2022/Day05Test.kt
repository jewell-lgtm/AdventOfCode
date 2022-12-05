package days.y2052


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day05 : Day(2022, 5) {
    override fun partOne(input: String): Any {
        val crates = Crates.from(input)
        instructions(input).forEach { instruction ->
            crates.moveSingly(instruction)
        }
        return crates.message()
    }

    override fun partTwo(input: String): Any {
        val crates = Crates.from(input)
        instructions(input).forEach { instruction ->
            crates.moveMany(instruction)
        }
        return crates.message()
    }

    fun instructions(input: String) = input.split("\n\n").last().lines().map { Instruction.from(it) }


    class Crates(val positions: MutableList<MutableList<String>>) {
        fun moveSingly(input: Instruction) {
            var toMove = input.quantity
            while (toMove > 0) {
                val toMoveIndex = positions[input.fromCol()].lastNonBlankIndex()
                val toLandIndex = positions[input.toCol()].lastNonBlankIndex() + 1
                val crate = positions[input.fromCol()][toMoveIndex]
                positions[input.fromCol()][toMoveIndex] = ""
                positions[input.toCol()][toLandIndex] = crate
                toMove--
            }
        }

        fun moveMany(input: Instruction) {
            val fromCol = input.fromCol()
            var toMoveIndex = positions[fromCol].lastNonBlankIndex()
            val toCol = input.toCol()
            var toLandIndex = positions[toCol].lastNonBlankIndex() + input.quantity
            var toMove = input.quantity
            while (toMove > 0) {
                val crate = positions[fromCol][toMoveIndex]
                positions[fromCol][toMoveIndex] = ""
                positions[toCol][toLandIndex] = crate
                toMove--
                toMoveIndex--
                toLandIndex--
            }
        }

        fun message() = positions.joinToString("") { it[it.lastNonBlankIndex()] }

        companion object {
            fun from(input: String): Crates {
                val (headerInput) = input.split("\n\n")
                val cratesInput = headerInput.lines().map { it.chunked(4) }
                val reversed = cratesInput.reversed().drop(1)
                val nRows = reversed.first().size
                val positions: MutableList<MutableList<String>> = MutableList(nRows) { MutableList(999) { "" } }
                reversed.forEachIndexed { lineIndex, line ->
                    line.forEachIndexed { rowIndex, s ->
                        if (s.trim().isNotEmpty()) {
                            positions[rowIndex][lineIndex] = s.trim()
                                .replace("[", "").replace("]", "")
                        }
                    }
                }
                return Crates(positions)
            }
        }
    }

    data class Instruction(val quantity: Int, val from: Int, val to: Int) {
        fun fromCol(): Int = from - 1
        fun toCol(): Int = to - 1

        companion object {
            fun from(input: String): Instruction {
                val (quantity, from, to) = input.split(" ").mapNotNull { it.toIntOrNull() }
                return Instruction(quantity, from, to)
            }
        }
    }


}

private fun List<String>.lastNonBlankIndex(): Int {
    var index = -1
    for (i in this.indices) {
        if (this[i].trim().isEmpty()) {
            return index
        } else {
            index = i
        }
    }
    error("no blank")
}


class Day05Test {
    private val exampleInput = """
                    [D]    
                [N] [C]    
                [Z] [M] [P]
                 1   2   3 
                
                move 1 from 2 to 1
                move 3 from 1 to 3
                move 2 from 2 to 1
                move 1 from 1 to 2
                """.trimIndent()

    @Test
    fun parsesInput() {
        val crates = Day05.Crates.from(exampleInput)
        assertThat(crates.positions[0][0], `is`("Z"))
        assertThat(crates.positions[0][1], `is`("N"))
        assertThat(crates.positions[0][2], `is`(""))
        assertThat(crates.positions[1][0], `is`("M"))
        assertThat(crates.positions[1][1], `is`("C"))
        assertThat(crates.positions[1][2], `is`("D"))
        assertThat(crates.positions[1][3], `is`(""))
        assertThat(crates.positions[2][1], `is`(""))
    }

    @Test
    fun moveCrates() {
        val crates = Day05.Crates.from(exampleInput)
        assertThat(crates.positions[1][2], `is`("D"))
        assertThat(crates.positions[0][2], `is`(""))
        crates.moveSingly(Day05.Instruction(1, 2, 1))
        assertThat(crates.positions[1][2], `is`(""))
        assertThat(crates.positions[0][2], `is`("D"))
        crates.moveSingly(Day05.Instruction(3, 1, 3))
        assertThat(crates.positions[2][0], `is`("P"))
        assertThat(crates.positions[2][1], `is`("D"))
        assertThat(crates.positions[2][2], `is`("N"))
        assertThat(crates.positions[2][3], `is`("Z"))
        assertThat(crates.positions[2][4], `is`(""))
        crates.moveSingly(Day05.Instruction(2, 2, 1))
        assertThat(crates.positions[0][0], `is`("C"))
        assertThat(crates.positions[0][1], `is`("M"))
        assertThat(crates.positions[1][0], `is`(""))
    }

    @Test
    fun testExampleOne() {
        assertThat(
            Day05().partOne(
                exampleInput
            ), `is`("CMZ")
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day05().partOne(), `is`("QPJPLMNNR"))
    }

    @Test
    fun moveCrates2() {
        val crates = Day05.Crates.from(exampleInput)
        assertThat(crates.positions[1][2], `is`("D"))
        assertThat(crates.positions[0][2], `is`(""))
        crates.moveMany(Day05.Instruction(1, 2, 1))
        assertThat(crates.positions[1][2], `is`(""))
        assertThat(crates.positions[0][2], `is`("D"))
        crates.moveMany(Day05.Instruction(3, 1, 3))
        assertThat(crates.positions[2][4], `is`(""))
        assertThat(crates.positions[2][3], `is`("D"))
        assertThat(crates.positions[2][0], `is`("P"))
        assertThat(crates.positions[2][2], `is`("N"))
        assertThat(crates.positions[2][1], `is`("Z"))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day05().partTwo(
                """
                    [D]    
                [N] [C]    
                [Z] [M] [P]
                 1   2   3 
                
                move 1 from 2 to 1
                move 3 from 1 to 3
                move 2 from 2 to 1
                move 1 from 1 to 2
                """.trimIndent()
            ), `is`("MCD")
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day05().partTwo(), `is`("BQDNWJPVJ"))
    }
}
