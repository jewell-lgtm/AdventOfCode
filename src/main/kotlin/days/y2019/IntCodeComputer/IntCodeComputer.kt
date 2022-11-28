package days.y2019.IntCodeComputer


class IntCodeComputer(
    val program: MutableList<Int>,
    var input: Int?,
) {
    val output: MutableList<Int> = mutableListOf()
    fun compute() {
        var pos = 0
        while (program.getOrNull(pos) != null) {
            val instruction = program[pos]
            val opcode = instruction.opcode
            val (mode1, mode2, mode3) = instruction.modes
            assert(mode3 == 0)
            when (instruction.opcode) {
                1 -> {
                    program[program[pos + 3]] = program.getValue(mode1, pos + 1) + program.getValue(mode2, pos + 2)
                    pos += 4
                }

                2 -> {
                    program[program[pos + 3]] = program.getValue(mode1, pos + 1) * program.getValue(mode2, pos + 2)
                    pos += 4
                }

                3 -> {
                    program[program[pos + 1]] = input!!
                    pos += 2
                }

                4 -> {
                    output.add(program.getValue(mode1, pos + 1))
                    pos += 2
                }

                5 -> {
                    if (program.getValue(mode1, pos + 1) != 0) {
                        pos = program.getValue(mode2, pos + 2)
                    } else {
                        pos += 3
                    }
                }

                6 -> {
                    if (program.getValue(mode1, pos + 1) == 0) {
                        pos = program.getValue(mode2, pos + 2)
                    } else {
                        pos += 3
                    }
                }

                7 -> {
                    program[program[pos + 3]] = if (program.getValue(mode1, pos + 1) < program.getValue(mode2, pos + 2)) 1 else 0
                    pos += 4
                }

                8 -> {
                    program[program[pos + 3]] = if (program.getValue(mode1, pos + 1) == program.getValue(mode2, pos + 2)) 1 else 0
                    pos += 4
                }

                99 -> {
                    pos = -1
                }

                else -> {
                    throw Exception("Unknown opcode $opcode")
                }
            }
        }
    }

    private val Int.opcode: Int
        get() = this % 100

    private val Int.modes: List<Int>
        get() = (this / 100).toString().padStart(3, '0').reversed().map { it.toString().toInt() }

    private fun List<Int>.getValue(mode: Int, index: Int): Int = if (mode == 0) this[this[index]] else this[index]

    companion object {
        fun from(integers: List<Int>, input: Int?): IntCodeComputer {
            return IntCodeComputer(integers.toMutableList(), input)
        }
    }
}





