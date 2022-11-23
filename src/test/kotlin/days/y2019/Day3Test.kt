package days.y2019

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNull.notNullValue
import org.junit.jupiter.api.Test

class Day3Test {

    private val dayOne = Day3()

    @Test
    fun testPartOne() {
        assertThat(dayOne.partOne(), `is`(721))
    }

    @Test
    fun testPartTwo() {
        assertThat(dayOne.partTwo(), `is`(7388))
    }
}
