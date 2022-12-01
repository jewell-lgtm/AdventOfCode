package days.y2022.day01

import days.Day

class Day01 : Day(2022, 1) {
    override fun partOne(input: String): Any = items(input).maxBy { it.sum() }


    override fun partTwo(input: String): Any =
        items(input).map { it.sum() }.sorted().takeLast(3).sum()

    private fun items(input: String) =
        input.split("\n\n").map { elfItinerary ->
            elfItinerary.split("\n").map { it.toInt() }
        }
}


