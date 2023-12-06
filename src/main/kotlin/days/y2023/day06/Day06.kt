package days.y2023.day06

import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day06(val input: PuzzleInput) {
    val races = raceDurations(input).zip(raceDistances(input))
    val longRace = parseLongRace(input)

    fun partOne() = races.map { waysToWin(it.first, it.second) }.product()
    fun partTwo() = waysToWin(longRace.first, longRace.second)

}

fun List<Long>.product(): Long {
    return this.reduce { acc, i -> acc * i }
}


fun raceDurations(input: PuzzleInput): List<Long> {
    return input[0].split(Regex("\\s+")).drop(1).map { it.toLong() }
}

fun raceDistances(input: PuzzleInput): List<Long> {
    return input[1].split(Regex("\\s+")).drop(1).map { it.toLong() }
}

fun parseLongRace(input: PuzzleInput): Pair<Long, Long> {
    val duration = input[0].split(": ")[1].trim().split(Regex("\\s+")).joinToString("").toLong()
    val distance = input[1].split(": ")[1].trim().split(Regex("\\s+")).joinToString("").toLong()

    return duration to distance
}


fun waysToWin(duration: Long, distance: Long): Long {


    val waysToWin =  ( 0..duration).filter { buttonHeldMs ->
        val remainingMs = duration - buttonHeldMs
        val speed = buttonHeldMs
        val travelledDistance = speed * remainingMs
        travelledDistance > distance
    }

    return waysToWin.size.toLong()
}

fun main() {
    val year = 2023
    val day = 6

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day06(input).partOne()
    fun partTwo(input: PuzzleInput) = Day06(input).partTwo()

    println("Example 1: ${partOne(exampleInput)}")
    println("Puzzle 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Puzzle 2: ${partTwo(puzzleInput)}")
}

