package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day11 : Day(2022, 11) {
    override fun partOne(input: String): Any {
        val monkeys = parseInput(input)

        repeat(20) {
            for (monkey in monkeys) {
                monkey.round(monkeys, 3)
            }
        }

        val (most, second) = monkeys.sortedByDescending { it.inspections }

        return (most.inspections * second.inspections).toString()
    }

    override fun partTwo(input: String): Any {
        val monkeys = parseInput(input)
        val monkeyProduct = monkeys.map { it.test }.product()

        repeat(10_000) {
            for (monkey in monkeys) {
                monkey.round(monkeys, 1, monkeyProduct)
            }
        }
        val (most, second) = monkeys.sortedByDescending { it.inspections }

        return (most.inspections * second.inspections).toString()
    }

    private fun List<ULong>.product() = this.fold(1uL) { acc, i -> acc * i }


    private fun parseInput(input: String) = input.split("\n\n")
        .map { monkeyStr ->
            Monkey.from(monkeyStr.trimIndent().lines())
        }

    class Monkey(
        val index: Int,
        val items: MutableList<ULong>,
        val operation: (ULong) -> ULong,
        val test: ULong,
        val ifTrueMonkeyIndex: Int,
        val ifFalseMonkeyIndex: Int,
        var inspections: ULong = 0uL
    ) {
        fun round(monkeys: List<Monkey>, relief: Int, monkeyProduct: ULong? = null) {
            items.indices.forEach { index ->
                items[index] = operation(items[index])
                items[index] = items[index] / relief.toULong()
                if (monkeyProduct != null) items[index] = items[index] % monkeyProduct
                if ((items[index]) % test == 0uL) {
                    monkeys[ifTrueMonkeyIndex].items.add(items[index])
                } else {
                    monkeys[ifFalseMonkeyIndex].items.add(items[index])
                }
            }
            inspections += items.size.toULong()
            items.clear()
        }


        companion object {
            fun from(lines: List<String>) = Monkey(
                monkeyNumber(lines[0]),
                monkeyItems(lines[1]).map { it.toULong() }.toMutableList(),
                monkeyOperation(lines[2]),
                monkeyNumber(lines[3]).toULong(),
                monkeyNumber(lines[4]),
                monkeyNumber(lines[5]),
            )


            private fun monkeyNumber(s: String): Int {
                val found =
                    Regex("(\\d+)").find(s) ?: throw IllegalArgumentException("Could not find monkey number in $s")
                return found.value.toInt()
            }

            private fun monkeyItems(s: String) =
                Regex("(\\d+)").findAll(s).map { it.value.toInt() }.toList()

            private fun monkeyOperation(s: String): (ULong) -> (ULong) {
                if (s.contains("old * old")) {
                    return { old -> old * old }
                }
                val operand = monkeyNumber(s).toULong()
                if (s.contains("*")) return { old -> old * operand }
                if (s.contains("+")) return { old -> old + operand }
                error("unknown operation $s")
            }


        }
    }

}


class Day11Test {

    val exampleInput = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
    """.trimIndent()

    @Test
    fun testExampleOne() {
        assertThat(Day11().partOne(exampleInput), `is`("10605"))
    }

    @Test
    fun testPartOne() {
        assertThat(Day11().partOne(), `is`("55458"))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day11().partTwo(exampleInput), `is`("2713310158")
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day11().partTwo(), `is`("14508081294"))
    }
}
