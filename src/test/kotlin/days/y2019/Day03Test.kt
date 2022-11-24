package days.y2019

import days.y2019.Day03
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day03Test {

    private val dayOne = Day03()

    @Test
    fun testPartOne() {
        assertThat(dayOne.partOne(), `is`(721))
    }

    @Test
    fun testPartTwo() {
        assertThat(dayOne.partTwo(), `is`(7388))
    }
}
