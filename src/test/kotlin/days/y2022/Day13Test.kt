package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day13 : Day(2022, 13) {
    override fun partOne(input: String): Any {
        // today's solution is in done in typescript
        return -1
    }

    override fun partTwo(input: String): Any {
        return -1
    }
}


class Day13Test {
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
        assertThat(Day13().partOne(exampleInput), `is`(-1))
    }

    @Test
    fun testPartOne() {
        assertThat(Day13().partOne(), `is`(-1))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day13().partTwo(
                """
            1
            2
            3
        """.trimIndent()
            ), `is`(-1)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day13().partTwo(), `is`(-1))
    }
}
