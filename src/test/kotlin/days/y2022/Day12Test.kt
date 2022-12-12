package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day12 : Day(2022, 12) {
    override fun partOne(input: String): Any {
        val (map, startPos, endPos) = parseInput(input)
        return findPath(map, startPos, endPos)
    }

    override fun partTwo(input: String): Any {
        val (map, _, endPos) = parseInput(input)
        val startPositions = map.scanForAll { it == 'S'.code || it == 'a'.code }

        return startPositions.minOf { findPath(map, it, endPos) }
    }

    fun parseInput(input: String): Triple<List<List<Int>>, Pair<Int, Int>, Pair<Int, Int>> {
        val lines = input.trim().lines()
        val map = lines.map { line -> line.trim().map { it.code } }
        val startPos = map.find { it == 'S'.code }
        val endPos = map.find { it == 'E'.code }
        return Triple(map, startPos, endPos)
    }


    fun <T> List<List<T>>.find(test: (T) -> Boolean) = scanForAll(test).first()

    fun <T> List<List<T>>.scanForAll(test: (T) -> Boolean) = mapIndexed { y, row ->
        row.mapIndexedNotNull { x, value -> if (test(value)) x to y else null }
    }.flatten()

        private

    fun findPath(map: List<List<Int>>, startPos: Pair<Int, Int>, endPos: Pair<Int, Int>): Int {
        val pathLengthTo = mutableMapOf(startPos to 0)
        val nodesToVisit = mutableSetOf(startPos)

        var currPos: Pair<Int, Int>

        while (nodesToVisit.isNotEmpty()) {
            currPos =
                nodesToVisit.minByOrNull { candidate -> pathLengthTo[candidate]?.let { it + 1 } ?: Int.MAX_VALUE }!!

            if (currPos == endPos) {
                return pathLengthTo[currPos]!!
            }
            nodesToVisit.remove(currPos)
            currPos.neighbors(map).forEach {
                val travelToNeighbor = pathLengthTo[currPos]!! + 1
                if (travelToNeighbor <= (pathLengthTo[it] ?: Int.MAX_VALUE)) {
                    pathLengthTo[it] = travelToNeighbor
                    nodesToVisit.add(it)
                }
            }
        }

        return Int.MAX_VALUE
    }

    private fun Pair<Int, Int>.neighbors(map: List<List<Int>>): List<Pair<Int, Int>> {
        return listOf(
            Pair(first - 1, second),
            Pair(first + 1, second),
            Pair(first, second - 1),
            Pair(first, second + 1)
        )
            .filter { it.first in map.indices && it.second in map[0].indices }
            .filter {
                val value = if (map[this.first][this.second] == 'S'.code) 'a'.code else map[this.first][this.second]
                val otherValue = if (map[it.first][it.second] == 'E'.code) 'z'.code else map[it.first][it.second]
                otherValue <= value + 1
            }
    }

}


class Day12Test {
    val exampleInput = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent()


    @Test
    fun testExampleOne() {
        assertThat(Day12().partOne(exampleInput), `is`(31))
    }

    @Test
    fun testPartOne() {
        assertThat(Day12().partOne(), `is`(440))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day12().partTwo(exampleInput), `is`(29)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day12().partTwo(), `is`(439))
    }
}
