package days.y2019

import days.y2019.Day05.Day05
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day05Test {

    private val dayOne = Day05()


    @Test
    fun testPartOne() {
        assertThat(dayOne.partOne(), `is`(7286649))
    }

    @Test
    fun testPartTwo() {
        assertThat(dayOne.partTwo(), `is`(15724522))
    }
}
