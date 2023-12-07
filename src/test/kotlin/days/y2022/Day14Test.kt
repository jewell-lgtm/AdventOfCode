package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.sign


class Day14 : Day(2022, 14) {
    override fun partOne(input: String): Any {
        val walls = parseInput(input)
        val collectedSand = mutableSetOf<Point>()
        try {
            while (true) {
                val sand = Point(500, 0)
                sand.fall(walls, collectedSand, null)
            }
        } catch (e: Exception) {
            println(e)
        }
        return collectedSand.size
    }

    override fun partTwo(input: String): Any {
        val deepest = parseInput(input).maxByOrNull { it.y }!!.y
        val floor = InfiniteLine(deepest + 2)

        val walls = parseInput(input)
        val collectedSand = mutableSetOf<Point>()
        try {
            while (true) {
                val sand = Point(500, 0)
                sand.fall(walls, collectedSand, floor)
            }
        } catch (e: Exception) {
            println(e)
        }
        return collectedSand.size
    }

    data class InfiniteLine(val y: Int)

    data class Point(val x: Int, val y: Int) {
        fun distance(that: Point) = (x - that.x).absoluteValue + (y - that.y).absoluteValue
        fun stepTowards(that: Point): Point = this + dir(that)
        private fun dir(that: Point) = (that - this).unit()
        private fun unit() = Point(x.sign, y.sign)
        private operator fun minus(that: Point) = Point(x - that.x, y - that.y)
        private operator fun plus(that: Point) = Point(x + that.x, y + that.y)

        companion object {
            fun from(first: Point) = Point(first.x, first.y)
        }
    }

    fun parseInput(input: String): Set<Point> {
        val result = mutableSetOf<Point>()
        input.lines().forEach { line ->
            line.split(" -> ").map { coord ->
                val (x, y) = coord.split(",").map { it.toInt() }
                Point(x, y)
            }.windowed(2).forEach { (first, second) ->
                var here = Point.from(first)
                result.add(here)
                while (here.distance(second) > 0) {
                    here = here.stepTowards(second)
                    result.add(here)
                }
            }
        }
        return result
    }

    private tailrec fun Point.fall(walls: Set<Point>, sand: MutableSet<Point>, floor: InfiniteLine?) {
        if (sand.contains(this)) {
            error("Full of sand.")
        }
        val deepest = walls.maxBy { it.y }
        if (floor == null && this.y > deepest.y) {
            error("We are falling into the abyss")
        }
        val down = Point(x, y + 1)
        if (!walls.contains(down) && !sand.contains(down) && (down.y < (floor?.y ?: Int.MAX_VALUE))) {
            return down.fall(walls, sand, floor)
        }
        val downLeft = Point(x - 1, y + 1)
        if (!walls.contains(downLeft) && !sand.contains(downLeft) && (down.y < (floor?.y ?: Int.MAX_VALUE))) {
            return downLeft.fall(walls, sand, floor)
        }
        val downRight = Point(x + 1, y + 1)
        if (!walls.contains(downRight) && !sand.contains(downRight) && (down.y < (floor?.y ?: Int.MAX_VALUE))) {
            return downRight.fall(walls, sand, floor)
        }
        sand.add(this)
    }

}


class Day14Test {
    val exampleInput = """
        498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent()

    @Test
    fun testExampleOne() {
        assertThat(Day14().partOne(exampleInput), `is`(24))
    }

    @Test
    fun testPartOne() {
        assertThat(Day14().partOne(), `is`(696))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day14().partTwo(exampleInput), `is`(93)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day14().partTwo(), `is`(23610))
    }
}
