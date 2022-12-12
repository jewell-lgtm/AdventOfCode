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
        val startPositions = map.scanAll { it == 'S'.code || it == 'a'.code }

        var best = Int.MAX_VALUE
        for (startPos in startPositions) {
            val path = findPath(map, startPos, endPos)
            if (path < best) {
                best = path
            }
        }

        return best
    }

    fun parseInput(input: String): Triple<List<List<Int>>, Pair<Int, Int>, Pair<Int, Int>> {
        val lines = input.trim().lines()
        val map = lines.map { line -> line.trim().map { it.code } }
        val startPos = map.scanFor { it == 'S'.code }
        val endPos = map.scanFor { it == 'E'.code }
        return Triple(map, startPos, endPos)
    }


    fun <T> List<List<T>>.scanFor(test: (T) -> Boolean) = scanAll(test).first()


    fun <T> List<List<T>>.scanAll(test: (T) -> Boolean): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (i in indices) {
            for (j in this[i].indices) {
                if (test(this[i][j])) {
                    result.add(i to j)
                }
            }
        }
        return result
    }

    private fun findPath(map: List<List<Int>>, startPos: Pair<Int, Int>, endPos: Pair<Int,Int>): Int {
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
            .filter { it.first in map.indices && it.second in map[it.first].indices }
            .filter { neighbor ->
                val value = map[this.first][this.second].takeUnless { it == 'S'.code } ?: 'a'.code
                val otherValue = map[neighbor.first][neighbor.second].takeUnless { it == 'E'.code } ?: 'z'.code
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
