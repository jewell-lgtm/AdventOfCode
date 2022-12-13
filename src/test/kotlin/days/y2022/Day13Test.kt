package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day00 : Day(2022, 0) {
    override fun partOne(input: String): Any {
        return -1
    }

    override fun partTwo(input: String): Any {
        return -1
    }
}


class Day00Test {
        val exampleInput = """
            [1,1,3,1,1]
            [1,1,5,1,1]
            
            [[1],[2,3,4]]
            [[1],4]
            
            [9]
            [[8,7,6]]
            
            [[4,4],4,4]
            [[4,4],4,4,4]
            
            [7,7,7,7]
            [7,7,7]
            
            []
            [3]
            
            [[[]]]
            [[]]
            
            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent()

    @Test
    fun testExampleOne() {
        assertThat(Day00().partOne(exampleInput), `is`(-1))
    }

    @Test
    fun testPartOne() {
        assertThat(Day00().partOne(), `is`(-1))
    }

    @Test
    fun testExampleTwo() {
        assertThat(Day00().partTwo("""
            1
            2
            3
        """.trimIndent()), `is`(-1))
    }
    @Test
    fun testPartTwo() {
        assertThat(Day00().partTwo(), `is`(-1))
    }
}
