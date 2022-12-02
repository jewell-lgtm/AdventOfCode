package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day02Test {

    @Test
    fun testExampleOne() {
        assertThat(
            Day02().partOne(
                """
            A Y
            B X
            C Z
        """.trimIndent()
            ), `is`(15)
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day02().partOne(), `is`(14264))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day02().partTwo(
                """
            A Y
            B X
            C Z
        """.trimIndent()
            ), `is`(12)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day02().partTwo(), `is`(12382))
    }
}


class Day02 : Day(2022, 2) {
    override fun partOne(input: String): Any {
        val throws = parseInput(input)
        return throws.sumOf { it.partOneScore() }
    }


    override fun partTwo(input: String): Any {
        val throws = parseInput(input)
        return throws.sumOf { it.partTwoScore() }
    }


    private fun parseInput(input: String): List<Pair<Char, Char>> = input.lines().map { line ->
        val (a, b) = line.split(" ")
        Pair(a[0], b[0])
    }

    private fun Pair<Char, Char>.partOneScore(): Int =
        when (first) {
            'A' -> when (second) {
                'X' -> 1 + 3
                'Y' -> 2 + 6
                'Z' -> 3 + 0
                else -> error("Unknown second: $second")
            }

            'B' -> when (second) {
                'X' -> 1 + 0
                'Y' -> 2 + 3
                'Z' -> 3 + 6
                else -> error("Unknown second: $second")
            }

            'C' -> when (second) {
                'X' -> 1 + 6
                'Y' -> 2 + 0
                'Z' -> 3 + 3
                else -> error("Unknown second: $second")
            }

            else -> error("Unknown first: $first")
        }

    private fun Pair<Char, Char>.partTwoScore(): Int =
        when (first) {
            'A' -> when (second) {
                'X' -> Pair('A', 'Z').partOneScore()
                'Y' -> Pair('A', 'X').partOneScore()
                'Z' -> Pair('A', 'Y').partOneScore()
                else -> error("Unknown second: $second")
            }

            'B' -> when (second) {
                'X' -> Pair('B', 'X').partOneScore()
                'Y' -> Pair('B', 'Y').partOneScore()
                'Z' -> Pair('B', 'Z').partOneScore()
                else -> error("Unknown second: $second")
            }

            'C' -> when (second) {
                'X' -> Pair('C', 'Y').partOneScore()
                'Y' -> Pair('C', 'Z').partOneScore()
                'Z' -> Pair('C', 'X').partOneScore()
                else -> error("Unknown second: $second")
            }

            else -> error("Unknown first: $first")

        }
}
