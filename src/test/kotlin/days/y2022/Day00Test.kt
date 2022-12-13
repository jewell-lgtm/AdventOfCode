package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day13 : Day(2022, 13) {
    override fun partOne(input: String): Any {
        val packets = parseInput(input)


        return -1
    }

    override fun partTwo(input: String): Any {
        return -1
    }


    abstract class Packet

    data class IntPacket(val value: Int)
    data class ListPacket(val value: List<Packet>)

    fun parseInput(input: String): List<Pair<Packet>>
}


class Day13Test {

    @Test
    fun testExampleOne() {
        assertThat(Day13().partOne("""
            1
            2
            3
        """.trimIndent()), `is`(-1))
    }

    @Test
    fun testPartOne() {
        assertThat(Day13().partOne(), `is`(-1))
    }

    @Test
    fun testExampleTwo() {
        assertThat(Day13().partTwo("""
            1
            2
            3
        """.trimIndent()), `is`(-1))
    }
    @Test
    fun testPartTwo() {
        assertThat(Day13().partTwo(), `is`(-1))
    }
}
