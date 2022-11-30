package days.y2019

import days.y2019.Day05.Day05
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day05Test {


    @Test
    fun testPartOne() {
        assertThat(Day05().partOne(), `is`(7286649))
    }

    @Test
    fun testPartTwo() {
        assertThat(Day05().partTwo(), `is`(15724522))
    }
}
