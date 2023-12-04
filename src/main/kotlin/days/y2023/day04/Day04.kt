package days.y2023.day04

import util.InputReader

class Day04(input: List<String>) {
    private val deck: List<Card> =
        input.mapNotNull { if (it.isNotEmpty()) Card(it) else null }

    fun partOne() = deck.sumOf { it.points() }
    fun partTwo(): Int {
        val count = deck.associateWith { 1 }.toMutableMap()
        deck.forEachIndexed { index, card ->
            val thisCount = count[card] ?: error("Card ${card.id} not found")
            for (i in 1..card.winners().size) {
                val nextCard = deck[index + i]
                count[nextCard] = count[nextCard]!! + thisCount
            }
        }
        return count.values.sum()
    }


    class Card(val id: Int, val winningNumbers: List<Int>, val gameNumbers: List<Int>)

    private fun Card.winners(): List<Int> = winningNumbers.filter { it in gameNumbers }

    // yes, I know, powers of 2 are a thing
    private fun Card.points(): Int = winners().fold(0) { acc, _ -> if (acc == 0) 1 else acc * 2 }

    private fun Card(input: String): Card {
        val (idStr, numStr) = input.split(": ")
        val id = idStr.split(Regex("\\s+"))[1].toInt()
        val (wN, nN) = numStr.split(" | ")
            .map { strPart ->
                strPart.trim().split(Regex("\\s+"))
                    .mapNotNull { if (it.trim().isNotEmpty()) it.toInt() else null }
            }
        return Card(id, wN, nN)
    }
}


fun main() {
    val exampleInput = InputReader.getExampleLines(2023, 4)
    val puzzleInput = InputReader.getInputLines(2023, 4)

    println("Part 1 example: ${Day04(exampleInput).partOne()}")
    println("Part 1: ${Day04(puzzleInput).partOne()}")
    println("Part 2 example: ${Day04(exampleInput).partTwo()}")
    println("Part 2: ${Day04(puzzleInput).partTwo()}")
}
