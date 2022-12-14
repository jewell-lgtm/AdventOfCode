package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.absoluteValue


class Day15(val bound: Int = 20) : Day(2022, 15) {
    override fun partOne(input: String): (rowToScan: Int) -> Int {
        val sensors = parseInput(input)
        val beaconPositions = sensors.map { it.closest.toString() }.toSet()
        val smallestX = sensors.minOf { it.xLowerBound }
        val largestX = sensors.maxOf { it.xUpperBound }
        return { y ->
            val filter = (smallestX..largestX).filter { x ->
                val sensorExclusionZone = sensors.any { sensor ->
                    Point(x, y).toString() !in beaconPositions && sensor.isInExclusionZone(
                        Point(
                            x,
                            y
                        )
                    )
                }
                sensorExclusionZone
            }
            filter.size
        }
    }


    override fun partTwo(input: String): Any {
        return -1
    }


    data class Point(val x: Int, val y: Int) {
        fun distance(that: Point) = (x - that.x).absoluteValue + (y - that.y).absoluteValue
        override fun toString() = "($x, $y)"
    }


    class Sensor(val pos: Point, val closest: Point) {
        val xLowerBound = pos.x - closest.distance(pos)
        val xUpperBound = pos.x + closest.distance(pos)

        fun isInExclusionZone(that: Point) = pos.distance(that) <= pos.distance(closest)

        // returns all positions that are x units away from the sensor
        fun allPositionsXAway(distance: Int): Set<Point> {
            val result = mutableSetOf<Point>()
            for (x in 0..distance) {
                val y = distance - x
                result.add(Point(x + pos.x, y + pos.y))
                result.add(Point(-x + pos.x, y + pos.y))
                result.add(Point(x + pos.x, -y + pos.y))
                result.add(Point(-x + pos.x, -y + pos.y))
            }
            return result
        }
    }

    fun parseInput(input: String): List<Sensor> {
        return input.trim().lines().mapNotNull { line ->
            Regex(".*x=(-?\\d+), y=(-?\\d+).*x=(-?\\d+), y=(-?\\d+)").find(line)?.groupValues?.let { groupValues ->
                val (x, y, closestX, closestY) = groupValues.drop(1).map { it.toInt() }
                Sensor(Point(x, y), Point(closestX, closestY))
            }
        }
    }

}


class Day15Test {
    val exampleInput = """
        Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        Sensor at x=9, y=16: closest beacon is at x=10, y=16
        Sensor at x=13, y=2: closest beacon is at x=15, y=3
        Sensor at x=12, y=14: closest beacon is at x=10, y=16
        Sensor at x=10, y=20: closest beacon is at x=10, y=16
        Sensor at x=14, y=17: closest beacon is at x=10, y=16
        Sensor at x=8, y=7: closest beacon is at x=2, y=10
        Sensor at x=2, y=0: closest beacon is at x=2, y=10
        Sensor at x=0, y=11: closest beacon is at x=2, y=10
        Sensor at x=20, y=14: closest beacon is at x=25, y=17
        Sensor at x=17, y=20: closest beacon is at x=21, y=22
        Sensor at x=16, y=7: closest beacon is at x=15, y=3
        Sensor at x=14, y=3: closest beacon is at x=15, y=3
        Sensor at x=20, y=1: closest beacon is at x=15, y=3
    """.trimIndent()

    @Test
    fun testExampleOne() {
        assertThat(
            Day15().partOne(exampleInput)(10), `is`(26)
        )
    }


    @Test
    fun testPartOne() {
        @Suppress("UNCHECKED_CAST")
        assertThat((Day15().partOne() as (Int) -> Int)(2000000), `is`(4724228))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day15().partTwo(exampleInput), `is`(56000011)
        )
    }

    @Test
    fun allSquaresXDistanceFrom() {
        val sens = Day15.Sensor(Day15.Point(5, 5), Day15.Point(10, 10))
        assertThat(sens.allPositionsXAway(0), `is`(setOf(Day15.Point(5, 5))))
        assertThat(
            sens.allPositionsXAway(1), `is`(
                setOf(
                    Day15.Point(5, 6),
                    Day15.Point(5, 4),
                    Day15.Point(6, 5),
                    Day15.Point(4, 5)
                )
            )
        )
        assertThat(
            sens.allPositionsXAway(2), `is`(
                setOf(
                    Day15.Point(5, 7),
                    Day15.Point(5, 3),
                    Day15.Point(6, 6),
                    Day15.Point(4, 6),
                    Day15.Point(7, 5),
                    Day15.Point(3, 5),
                    Day15.Point(6, 4),
                    Day15.Point(4, 4)
                )
            )
        )
    }

    @Test
    fun testPartTwo() {
        @Suppress("UNCHECKED_CAST")
        assertThat(
            Day15(4000000).partTwo(), `is`(-1)
        )
    }
}
