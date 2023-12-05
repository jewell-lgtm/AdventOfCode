package days.y2023.day05

import util.InputReader

typealias Seed = Long
typealias PuzzleInput = String

class Day05(val input: PuzzleInput) {
    val seeds = parseSeeds(input)
    val seedRanges = parseSeedRanges(input)
    val remappers = parseRemappers(input)

    fun partOne() = seeds.minOf { mapSeed(it) }

    fun partTwo(): Long {
        println("Checking ${seedRangeSize()} seeds")
        return seedRanges.minOf { range ->
            range.minOf { mapSeed(it) }
        }
    }

    fun seedRangeSize() = seedRanges.sumOf { it.last - it.first }.let { it to "${(it / 1_000_000L)} million" }


    var i = 0L
    fun mapSeed(seed: Seed): Seed {
        i++
        if (i % 1_000_000 == 0L) {
            println("i: ${i / 1_000_000} million of ${seedRangeSize().second} seeds")
        }
        var result: Seed = seed
        var nextRemapper: Remapper? = remappers.find { it.from == "seed" }!!

        while (nextRemapper != null) {
            result = nextRemapper.map(result)
            nextRemapper = remappers.find { it.from == nextRemapper!!.to }
        }


        return result
    }
}

data class Remapper(val from: String, val to: String, val ranges: Map<LongRange, LongRange>) {
    fun map(input: Long): Long {
        val mapEntry = ranges.entries.firstOrNull { it.value.contains(input) }
        if (mapEntry == null) {
            return input
        }
        val (destination, source) = mapEntry
        val offset = input - source.first
        return destination.first + offset
    }
}

private fun parseRemappers(input: PuzzleInput): List<Remapper> {
    val chunks = input.trim().lines().drop(2).joinToString("\n").split("\n\n")
    return chunks.map { chunk ->
        val (header, rest) = chunk.split("\n", limit = 2)
        val restLines = rest.lines()
        val (from, _, to) = header.split(" ")[0].split("-")
        val pairs = restLines.map {
            val (destinationBegin, sourceBegin, length) = it.split(" ").map { it.toLong() }
            (destinationBegin until destinationBegin + length) to (sourceBegin until sourceBegin + length)
        }
        Remapper(from, to, pairs.toMap())
    }
}

private fun parseSeeds(input: PuzzleInput): List<Long> =
    input.trim().lines().first().split("seeds: ")[1].run { split(" ").map { it.toLong() } }

// seeds: 79 14 55 13
private fun parseSeedRanges(input: PuzzleInput): List<LongRange> =
    input.trim().lines().first().split("seeds: ")[1]
        .run { split(" ").map { it.toLong() } }
        .chunked(2)
        .map { (start, length) -> start until start + length }

fun main() {
    val year = 2023
    val day = 5

    val exampleInput: PuzzleInput = InputReader.getExample(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleInput(year, day)

    fun partOne(input: PuzzleInput): Long = Day05(input).partOne()
    fun partTwo(input: PuzzleInput): Long = Day05(input).partTwo()

    /*
    Seed number 79 corresponds to soil number 81.
    Seed number 14 corresponds to soil number 14.
    Seed number 55 corresponds to soil number 57.
    Seed number 13 corresponds to soil number 13.
     */
//    val seedSoilRemapper = parseRemappers(exampleInput).find { it.from == "seed" && it.to == "soil" }!!
//    println("Seed number 79 corresponds to soil number ${seedSoilRemapper.map(79L)}")
//    println("Seed number 14 corresponds to soil number ${seedSoilRemapper.map(14L)}")
//    println("Seed number 55 corresponds to soil number ${seedSoilRemapper.map(55L)}")
//    println("Seed number 13 corresponds to soil number ${seedSoilRemapper.map(13L)}")
//    println("Seed number 53 corresponds to soil number ${seedSoilRemapper.map(53L)}")

//    println("Seed 79 ${Day05(exampleInput).mapSeed(79L)}")
//    println("Seed 14 ${Day05(exampleInput).mapSeed(14L)}")
//    println("Seed 55 ${Day05(exampleInput).mapSeed(55L)}")
//    println("Seed 13 ${Day05(exampleInput).mapSeed(13L)}")

//    println(Day05(exampleInput).seedRanges)


    println("Example 1: ${partOne(exampleInput)}")
    println("Part 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Part 2: ${partTwo(puzzleInput)}")
}

