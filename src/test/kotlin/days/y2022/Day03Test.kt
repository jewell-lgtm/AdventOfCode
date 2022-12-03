package days.y2032


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test


class Day03Test {

    @Test
    fun scores() {
        assertThat('a'.itemScore(), `is`(1))
        assertThat('z'.itemScore(), `is`(26))
        assertThat('A'.itemScore(), `is`(27))
        assertThat('Z'.itemScore(), `is`(52))
    }

    @Test
    fun testExampleOne() {
        assertThat(
            Day03().partOne(
                """
                vJrwpWtwJgWrhcsFMMfFFhFp
                jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
                PmmdzqPrVvPwwTWBwg
                wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
                ttgJtRGJQctTZtZT
                CrZsJsPPZsGzwwsLwLmpwMDw    
                """.trimIndent()
            ), `is`(157)
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day03().partOne(), `is`(7817))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day03().partTwo(
                """
                vJrwpWtwJgWrhcsFMMfFFhFp
                jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
                PmmdzqPrVvPwwTWBwg
                wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
                ttgJtRGJQctTZtZT
                CrZsJsPPZsGzwwsLwLmpwMDw    
                """.trimIndent()
            ), `is`(70)
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day03().partTwo(), `is`(2444))
    }
}


class Day03 : Day(2022, 3) {
    override fun partOne(input: String): Any =
        parseInput(input).sumOf { it.rucksackScore() }


    override fun partTwo(input: String): Any =
        parseInput2(input).windowsScore()


    private fun parseInput(input: String): List<String> = input.trim().lines()
    private fun parseInput2(input: String): List<Triple<String, String, String>> =
        input.trim().lines().chunked(3).map { (a, b, c) ->
            Triple(a, b, c)
        }


}

private fun Triple<String, String, String>.intersection(): Set<Char> =
    first.toList().intersect(second.toSet()).intersect(third.toSet())

private fun String.rucksackScore(): Int {
    val firstHalf = this.substring(0, this.length / 2)
    val secondHalf = this.substring(this.length / 2)
    val appearsInBoth = firstHalf.toList().intersect(secondHalf.toList().toSet())
    return appearsInBoth.toCharArray().sumOf { it.itemScore() }
}

private fun Char.itemScore(): Int = when (code) {
    in 'a'.code..'z'.code -> code - 'a'.code + 1
    in 'A'.code..'Z'.code -> code - 'A'.code + 27
    else -> error("wtf $this")
}

private fun List<Triple<String, String, String>>.windowsScore(): Int =
    sumOf { chunk -> chunk.intersection().sumOf { item -> item.itemScore() } }

