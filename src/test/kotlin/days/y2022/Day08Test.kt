package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day08 : Day(2022, 8) {
    override fun partOne(input: String): Any {
        val grid = parseInput(input)
        return grid
            .cells()
            .count { (y, x) -> isVisible(grid, y, x) }
    }

    private val directions = listOf(
        -1 to 0,
        0 to -1,
        1 to 0,
        0 to 1,
    )

    fun isVisible(grid: List<List<Int>>, y: Int, x: Int): Boolean {
        val curr = grid[y][x]
        if (y == 0 || x == 0) return true

        for (direction in directions) {
            val (dy, dx) = direction
            var y2 = y + dy
            var x2 = x + dx
            var isVisible = true
            while (isVisible && y2 >= 0 && x2 >= 0 && y2 < grid.size && x2 < grid[y2].size) {
                val next = grid[y2][x2]
                if (next >= curr) isVisible = false
                y2 += dy
                x2 += dx
            }
            if (isVisible) return true
        }

        return false
    }

    override fun partTwo(input: String): Any {
        val grid = parseInput(input)
        return grid.cells().maxOf { (y, x) -> scenicScore(grid, y, x) }
    }

    fun scenicScore(grid: List<List<Int>>, y: Int, x: Int): Int {
        val height = grid[y][x]
        val scores = directions.map { (dy, dx) ->
            var y2 = y + dy
            var x2 = x + dx
            var score = 0
            while (y2 >= 0 && x2 >= 0 && y2 < grid.size && x2 < grid[y2].size) {
                val next = grid[y2][x2]
                score++
                if (next >= height) break
                y2 += dy
                x2 += dx
            }
            score
        }
        return scores.product()
    }

    private fun List<List<Int>>.cells(): Set<Pair<Int, Int>> =
        indices.flatMap { y ->
            this[y].indices.map { x -> y to x }
        }.toSet()


    fun parseInput(input: String): List<List<Int>> = input.lines().map { line ->
        line.split("").filter { it.isNotBlank() }.map { char ->
            char.toInt()
        }
    }
}

private fun List<Int>.product(): Int = this.fold(1) { acc, i -> acc * i }


class Day08Test {

    private val example = """
                30373
                25512
                65332
                33549
                35390
            """.trimIndent()

    @Test
    fun isVisible() {
        listOf(
            Pair(0 to 0, true),
            Pair(1 to 1, true),
            Pair(1 to 3, false),
            Pair(2 to 1, true),
            Pair(2 to 2, false),
            Pair(2 to 3, true),
            Pair(3 to 2, true),
            Pair(3 to 1, false),
            Pair(3 to 3, false),
        ).forEach { (pos, expected) ->
            val (y, x) = pos
            val grid = Day08().parseInput(example)
            assertThat(Day08().isVisible(grid, y, x), `is`(expected))
        }
    }

    @Test
    fun testExampleOne() {
        assertThat(Day08().partOne(example), `is`(21))
    }

    @Test
    fun testPartOne() {
        assertThat(Day08().partOne(), `is`(1805))
    }

    @Test
    fun scenicScore() {
        listOf(
            Pair(1 to 2, 4),
            Pair(3 to 2, 8),
        ).forEach { (pos, expected) ->
            val (y, x) = pos
            val grid = Day08().parseInput(example)
            assertThat(Day08().scenicScore(grid, y, x), `is`(expected))
        }
    }

    @Test
    fun testExampleTwo() {
        assertThat(Day08().partTwo(example), `is`(8))
    }

    @Test
    fun testPartTwo() {
        assertThat(Day08().partTwo(), `is`(444528))
    }
}
