package days.y2022


import days.y2022.day02.Day02
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day02Test {

    @Test
    fun testExampleOne() {
        assertThat(Day02().partOne("""
            1
            2
            3
        """.trimIndent()), `is`(-1))
    }

    @Test
    fun testPartOne() {
        assertThat(Day02().partOne(), `is`(-1))
    }

    @Test
    fun testExampleTwo() {
        assertThat(Day02().partTwo("""
            1
            2
            3
        """.trimIndent()), `is`(-1))
    }

    @Test
    fun testPartTwo() {
        assertThat(Day02().partTwo(), `is`(-1))
    }
}