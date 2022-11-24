package days.y2019

import days.y2019.Day04.Day04
import days.y2019.Day04.isValid
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Day04Test {

    private val dayOne = Day04()

    @Test
    fun testIsValid() {
        listOf("111111").forEach {
            assertTrue(it.isValid())
        }
    }

    @Test
    fun testPartOne() {
        assertThat(dayOne.partOne(), `is`(2081))
    }

    @Test
    fun testPartTwo() {
        assertThat(dayOne.partTwo(), `is`(1411))
    }
}
