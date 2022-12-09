package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue


class Day09 : Day(2022, 9) {
    override fun partOne(input: String): Any {
        val directions = parseInput(input)
        val visited = mutableSetOf(Pair(0, 0))
        var head = Pair(0, 0)
        var tail = Pair(0, 0)

        for (direction in directions) {
            val (dx, dy) = direction.direction
            var toTravel = direction.distance
            while (toTravel > 0) {
                head += Pair(dx, dy)
                tail = tail.follow(head)
                visited.add(tail)
                toTravel--
            }
        }
        return visited.size
    }


    override fun partTwo(input: String): Any {
        val directions = parseInput(input)
        val snake = MutableList(10) { Pair(0, 0) }
        val visited = mutableSetOf(Pair(0, 0))
        for (direction in directions) {
            var toTravel = direction.distance
            while (toTravel > 0) {
                snake[0] += direction.direction
                // pretty sure most of the snake could stay still
                for (segment in snake.indices.drop(1)) {
                    snake[segment] = snake[segment].follow(snake[segment - 1])
                }
                visited.add(snake.last())
                toTravel--
            }
        }
        return visited.size
    }


    private fun parseInput(input: String): List<Instruction> = input.lines().map { line -> Instruction.from(line) }

    data class Instruction(val direction: Pair<Int, Int>, val distance: Int) {
        companion object {
            val directions = mapOf(
                'U' to Pair(0, -1),
                'D' to Pair(0, 1),
                'L' to Pair(-1, 0),
                'R' to Pair(1, 0),
            )

            fun from(line: String): Instruction {
                val vec = directions[line[0]] ?: error("Invalid direction: ${line[0]}")
                val distance = line.substring(2).toInt()
                return Instruction(vec, distance)
            }
        }
    }


    private operator fun Pair<Int, Int>.plus(that: Pair<Int, Int>): Pair<Int, Int> =
        Pair(this.first + that.first, this.second + that.second)

    private val Pair<Int, Int>.allNeighborsAndMe: Set<Pair<Int, Int>>
        get() = setOf(
            Pair(first - 1, second - 1),
            Pair(first, second - 1),
            Pair(first + 1, second - 1),
            Pair(first - 1, second),
            Pair(first, second),
            Pair(first + 1, second),
            Pair(first - 1, second + 1),
            Pair(first, second + 1),
            Pair(first + 1, second + 1)
        )


    // pretty sure I could compute this rather than looping
    fun Pair<Int, Int>.follow(that: Pair<Int, Int>): Pair<Int, Int> =
        if (that.allNeighborsAndMe.contains(this)) this
        else this.allNeighborsAndMe.minBy { it.distance(that) }

    fun Pair<Int, Int>.distance(that: Pair<Int, Int>): Int =
        (this.first - that.first).absoluteValue + (this.second - that.second).absoluteValue

}


class Day09Test {

    @Test
    fun testExampleOne() {
        assertThat(
            Day09().partOne(
                """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()
            ), `is`(13)
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day09().partOne(), `is`(6498))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day09().partTwo(
                """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
        """.trimIndent()
            ), `is`(36)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day09().partTwo(), `is`(2531))
    }
}
