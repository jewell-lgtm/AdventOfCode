package days.y2022


import days.Day
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import java.lang.Integer.max
import kotlin.math.absoluteValue


class Day15(val bound: Int = 20) : Day(2022, 15) {
    override fun partOne(input: String): (rowToScan: Int) -> Int {
        val sensors = parseInput(input.takeUnless { it.isBlank() } ?: inputString)
        return { rowToScan ->
            val beaconPositions =
                sensors.mapNotNull { sensor -> sensor.closest.x.takeIf { sensor.closest.y == rowToScan } }.toSet()
            val exclusionZonePoints = sensors.mapNotNull { sensor ->
                sensor.exclusionZone(rowToScan)
            }
            (exclusionZonePoints.smallest()..exclusionZonePoints.largest()).count { x ->
                !beaconPositions.contains(x) && exclusionZonePoints.any { it.contains(x) }
            }
        }
    }


    override fun partTwo(input: String): Any {
        val sensors = parseInput(input)

        for (y in (0..bound)) {
            val zones = sensors.mapNotNull { sensor -> sensor.exclusionZone(y) }
            val xBounds = (zones.smallest() + 1) until zones.largest()

            val oneToTheLeft = zones.map { it.first }.map { it - 1 }
            val oneToTheRight = zones.map { it.last }.map { it + 1 }
            val searchXs = listOf(oneToTheLeft, oneToTheRight).flatten()

            for (x in searchXs) {
                if (xBounds.contains(x) && zones.none { it.contains(x) }) {
                    return (x * 4000000L) + y
                }
            }

        }

        error("no result found")
    }


    data class Point(val x: Int, val y: Int) {
        fun distance(that: Point) = (x - that.x).absoluteValue + (y - that.y).absoluteValue
        override fun toString() = "($x, $y)"
    }


    data class Sensor(val pos: Point, val closest: Point) {
        val distance = pos.distance(closest)
    }


    fun parseInput(input: String): List<Sensor> {
        if (input.isBlank() && inputString.isNotBlank()) return parseInput(inputString)
        return input.trim().lines().mapNotNull { line ->
            Regex(".*x=(-?\\d+), y=(-?\\d+).*x=(-?\\d+), y=(-?\\d+)").find(line)?.groupValues?.let { groupValues ->
                val (x, y, closestX, closestY) = groupValues.drop(1).map { it.toInt() }
                Sensor(Point(x, y), Point(closestX, closestY))
            }
        }
    }

}


private fun List<IntRange>.smallest() = minOf { it.first }
private fun List<IntRange>.largest() = maxOf { it.last }

fun Day15.Sensor.exclusionZone(atY: Int): IntRange? {
    val offset = (atY - pos.y).absoluteValue
    val size = max(0, (distance - offset))
    if (size == 0) return null
    return (pos.x - size)..(pos.x + size)
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
        assertThat(Day15().partOne("")(2000000), `is`(4724228))
    }

    @Test
    fun testExclusionZone() {
        val sensor = Day15.Sensor(Day15.Point(8, 7), Day15.Point(2, 10))

        assertThat(sensor.exclusionZone(10000), `is`(nullValue()))
        assertThat(sensor.exclusionZone(7), `is`(-1..17))

        assertThat(sensor.exclusionZone(6), `is`(0..16))
        assertThat(sensor.exclusionZone(5), `is`(1..15))

        assertThat(sensor.exclusionZone(8), `is`(0..16))
        assertThat(sensor.exclusionZone(9), `is`(1..15))
    }


    @Test
    fun testExampleTwo() {
        assertThat(
            Day15(20).partTwo(exampleInput), `is`(56000011L)
        )
    }


    @Test
    fun testPartTwo() {
        assertThat(
            Day15(4000000).partTwo(""), `is`(13622251246513L)
        )
    }
}
