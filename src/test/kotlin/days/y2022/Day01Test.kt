package days.y2022


import days.y2022.Day01.Day01
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day01Test {

    @Test
    fun testExampleOne() {
        assertThat(Day01().partOne("""
            1000
            2000
            3000
            
            4000
            
            5000
            6000
            
            7000
            8000
            9000
            
            10000
        """.trimIndent()), `is`(24000))
    }

    @Test
    fun testPartOne() {
        assertThat(Day01().partOne(), `is`(70720))
    }


    @Test
    fun testPartTwo() {
        assertThat(Day01().partTwo(), `is`(207148))
    }
}