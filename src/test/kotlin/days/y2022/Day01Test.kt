package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day01 : Day(2022, 1) {
    override fun partOne(input: String): Any = items(input).maxOf { it.sum() }

    override fun partTwo(input: String): Any =
        items(input).map { it.sum() }.sorted().takeLast(3).sum()

    private fun items(input: String) =
        input.split("\n\n").map { elfItinerary ->
            elfItinerary.split("\n").map { it.toInt() }
        }
}

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

