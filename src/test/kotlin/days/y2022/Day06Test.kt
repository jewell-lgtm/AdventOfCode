package days.y2062


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day06 : Day(2022, 6) {
    override fun partOne(input: String): Any {
        val buffer = MutableList<Char?>(4) { null }
        input.forEachIndexed { index, c ->
            buffer[index % 4] = c
            if (buffer.filterNotNull().toSet().size == 4) return index + 1
        }
        return -1
    }

    override fun partTwo(input: String): Any {
        val buffer = MutableList<Char?>(14) { null }
        input.forEachIndexed { index, c ->
            buffer[index % 14] = c
            if (buffer.filterNotNull().toSet().size == 14) return index + 1
        }
        return -1
    }
}


class Day06Test {
    private val exampleInput = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"


    @Test
    fun testExampleOne() {
        assertThat(
            Day06().partOne(exampleInput), `is`(7)
        )

        mapOf(
            "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
            "nppdvjthqldpwncqszvftbrmjlhg" to 6,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11,
        ).entries.forEach { (input, expected) ->
            assertThat(Day06().partOne(input), `is`(expected))
        }
    }

    @Test
    fun testPartOne() {
        assertThat(Day06().partOne(), `is`(1816))
    }

    @Test
    fun testPartTwo() {
        assertThat(Day06().partTwo(), `is`(2625))
    }
}
