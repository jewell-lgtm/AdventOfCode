package days.y2023.day02

import util.InputReader


fun partOne(input: String): Int {
    val parsed = parseInput(input)
    // The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?
    val possible = parsed.filter { it.possible(12, 13, 14) }
    return possible.sumOf { it.id }
}

fun partTwo(input: String): Int {
    val parsed = parseInput(input)
    return parsed.map { game ->
        val red = game.sets.maxOf { it.countRed }
        val green = game.sets.maxOf { it.countGreen }
        val blue = game.sets.maxOf { it.countBlue }
        Triple(red, green, blue)
    }.sumOf { (red, green, blue) -> red * green * blue }

}

private fun Game.possible(red: Int, green: Int, blue: Int): Boolean {
    return sets.all { set ->
        set.countRed <= red && set.countGreen <= green && set.countBlue <= blue
    }
}


private fun parseInput(input: String): List<Game> {
    return input.lines().filter { it.isNotEmpty() }.map { line -> Game.from(line) }
}

private data class Game(val line: String, val id: Int, val sets: List<GameSet>) {
    companion object {
        fun from(line: String): Game {
            val match = Regex("Game (?<id>\\d+): (?<sets>.*)").find(line)?.groups ?: error("Did not match: $line")
            val id = match.getInt("id")
            val sets = match.getString("sets").split("; ").map { set -> GameSet.from(set) }
            return Game(line, id, sets)
        }
    }
}

private data class GameSet(val countBlue: Int, val countRed: Int, val countGreen: Int) {
    companion object {
        fun from(line: String): GameSet {
            val chunks = line.split(", ")
            val countBlue = chunks.blueChunk()
            val countRed = chunks.redChunk()
            val countGreen = chunks.greenChunk()
            return GameSet(countBlue ?: 0, countRed ?: 0, countGreen ?: 0)
        }
    }
}

private fun List<String>.blueChunk(): Int? =
    firstNotNullOfOrNull { Regex("(?<countBlue>\\d+) blue").find(it)?.groups }?.getInt("countBlue")

private fun List<String>.redChunk(): Int? =
    firstNotNullOfOrNull { Regex("(?<countRed>\\d+) red").find(it)?.groups }?.getInt("countRed")

private fun List<String>.greenChunk(): Int? =
    firstNotNullOfOrNull { Regex("(?<countGreen>\\d+) green").find(it)?.groups }?.getInt("countGreen")

private fun MatchGroupCollection.getInt(name: String) = this[name]?.value?.toInt() ?: error(name)
private fun MatchGroupCollection.getString(name: String) = this[name]?.value ?: error(name)

fun main(args: Array<String>) {
    println("Example One: ${partOne(InputReader.getExample(2023, 2))}")
    println("Part One: ${partOne(InputReader.getInput(2023, 2))}")
    println("Part Two: ${partTwo(InputReader.getInput(2023, 2))}")
}
