package days.y2042


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day04 : Day(2022, 4) {
    override fun partOne(input: String): Any =
        parseInput(input).filter { (a, b) ->
            a.fullyContains(b) || b.fullyContains(a)
        }.size


    override fun partTwo(input: String): Any =
        parseInput(input).filter { (a, b) ->
            a.overlaps(b)
        }.size


    private fun parseInput(input: String): List<Pair<IntRange, IntRange>> =
        input.trim().lines().map { line ->
            Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)").find(line)?.destructured?.let { (a, b, c, d) ->
                a.toInt()..b.toInt() to c.toInt()..d.toInt()
            } ?: error("Invalid input: $line")
        }

    private fun IntRange.fullyContains(other: IntRange): Boolean = this.first <= other.first && this.last >= other.last


    private fun IntRange.overlaps(other: IntRange): Boolean =
        this.first <= other.first && this.last >= other.first ||
                other.first <= this.first && other.last >= this.first


}


class Day04Test {
    @Test
    fun testExampleOne() {
        assertThat(
            Day04().partOne(
                """
                2-4,6-8
                2-3,4-5
                5-7,7-9
                2-8,3-7
                6-6,4-6
                2-6,4-8  
                """.trimIndent()
            ), `is`(2)
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day04().partOne(), `is`(547))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day04().partTwo(
                """
                2-4,6-8
                2-3,4-5
                5-7,7-9
                2-8,3-7
                6-6,4-6
                2-6,4-8  
                """.trimIndent()
            ), `is`(4)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day04().partTwo(), `is`(843))
    }
}
