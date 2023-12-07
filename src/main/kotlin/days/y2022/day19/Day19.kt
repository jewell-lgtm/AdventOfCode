package days.y2022.day19

import util.InputReader
import kotlin.math.ceil

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day19(val input: PuzzleInput) {
    val bluePrints = input.map { it.toBlueprint() }

    fun partOne(): Int {
        val initialState = GameState.initial()
        val blueprint = bluePrints.first()
        return blueprint.bestStateFrom(
            mutableMapOf(), GameState(
                remainingMinutes = 24,
                countOreRobots = 1,
                ore = 0,
                countClayRobots = 0,
                clay = 0,
                countObsidianRobots = 0,
                obsidian = 0,
                countGeodeRobots = 0,
                geodes = 0
            )
        ).geodes
    }

}

data class GameState(
    val remainingMinutes: Int = 0,
    val countOreRobots: Int = 0,
    val countClayRobots: Int = 0,
    val countObsidianRobots: Int = 0,
    val countGeodeRobots: Int = 0,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geodes: Int = 0,
) {
    companion object {
        fun initial() = GameState(
            remainingMinutes = 24,
            countOreRobots = 1,
        )
    }
}


sealed class RobotCost
data class OreCost(val ore: Int) : RobotCost()
data class OreClayCost(val ore: Int, val clay: Int) : RobotCost()
data class OreObsidianCost(val ore: Int, val obsidian: Int) : RobotCost()

data class Blueprint(
    val id: Int,
    val oreRobotCost: OreCost,
    val clayRobotCost: OreCost,
    val obsidianRobotCost: OreClayCost,
    val geodeRobotCost: OreObsidianCost,
)

var hit = 0
var miss = 0
var i = 0
fun Blueprint.bestStateFrom(memo: MutableMap<GameState, GameState>, state: GameState): GameState {
    val missBefore = miss
    val result = memo.getOrPut(state) {
        miss++
        val possibleNextStates = state.possibleNextStates(this)
        possibleNextStates.maxByOrNull { bestStateFrom(memo, it).geodes } ?: state
    }
    if (miss > missBefore && i++ % 10 == 0) {
        println("Miss ${miss++}")
    } else {
        hit++
    }

    return result
}


fun GameState.possibleNextStates(blueprint: Blueprint): Set<GameState> {
    if (remainingMinutes == 0) return emptySet()
    val result = mutableSetOf<GameState>()
    buyClayRobot(blueprint)?.let { result.add(it) }
    buyGeodeRobot(blueprint)?.let { result.add(it) }
    result.add(doNothing())
    return result
}

fun GameState.doNothing(): GameState = copy(
    remainingMinutes = 0,
    ore = ore + (remainingMinutes * countOreRobots),
    clay = clay + (remainingMinutes * countClayRobots),
    obsidian = obsidian + (remainingMinutes * countObsidianRobots),
    geodes = geodes + (remainingMinutes * countGeodeRobots)
)

fun OreCost.oreTurnsRequired(currentOre: Int, orePerTurn: Int): Int? {
    if (orePerTurn == 0) {
        if (currentOre > ore) return 0
        return null
    }
    return ((ore - currentOre).toFloat() / orePerTurn.toFloat()).ceilToInt().coerceAtLeast(0)
}

val Blueprint.maxClayCost get() = obsidianRobotCost.clay

fun GameState.buyClayRobot(blueprint: Blueprint): GameState? {
    if (blueprint.maxClayCost <= countClayRobots) return null
    if (blueprint.maxClayCost <= clay) return null
    val oreTurnsRequired =
        blueprint.clayRobotCost.oreTurnsRequired(ore, countOreRobots + countClayRobots)
            ?: return null

    if (oreTurnsRequired > (remainingMinutes - 2)) return null

    // wait 2 minutes deciding to build the robot (+ 2 ore, - 2 minutes)
    // spend 1 minute building the robot (+ 1 ore, - 1 minute)
    // land on the next minute, so we can build the robot (+ 1 ore, +1 clay, - 1 minute)

    return copy(
        remainingMinutes = remainingMinutes - oreTurnsRequired - 2,
        countClayRobots = countClayRobots + 1,
        ore = ore + (countOreRobots * (oreTurnsRequired + 2)) - blueprint.clayRobotCost.ore,
        clay = clay + (countClayRobots * oreTurnsRequired) + 1,
    )
}

fun OreObsidianCost.oreTunsRequired(currentOre: Int, orePerTurn: Int): Int? {
    if (orePerTurn == 0) {
        if (currentOre > ore) return 0
        return null
    }
    return ((ore - currentOre).toFloat() / orePerTurn.toFloat()).ceilToInt().coerceAtLeast(0)
}

fun OreObsidianCost.obsidianTurnsRequired(currentObsidian: Int, obsidianPerTurn: Int): Int? {
    if (obsidianPerTurn == 0) {
        if (currentObsidian > obsidian) return 0
        return null
    }
    return ((obsidian - currentObsidian).toFloat() / obsidianPerTurn.toFloat()).ceilToInt().coerceAtLeast(0)
}

fun GameState.buyGeodeRobot(blueprint: Blueprint): GameState? {
    val oreTurnsRequired =
        blueprint.geodeRobotCost.oreTunsRequired(ore, countOreRobots + countClayRobots + countObsidianRobots)
            ?: return null
    val obsidianTurnsRequired =
        blueprint.geodeRobotCost.obsidianTurnsRequired(obsidian, countObsidianRobots) ?: return null

    val turnsRequired = maxOf(oreTurnsRequired, obsidianTurnsRequired)
    if (turnsRequired >= (remainingMinutes - 1)) return null

    return copy(
        remainingMinutes = remainingMinutes - turnsRequired,
        countGeodeRobots = countGeodeRobots + 1,
        ore = ore + (countOreRobots * turnsRequired) - blueprint.geodeRobotCost.ore,
        clay = clay + (countClayRobots * turnsRequired),
        obsidian = obsidian + (countObsidianRobots * turnsRequired) - blueprint.geodeRobotCost.obsidian,
        geodes = geodes + (countGeodeRobots * turnsRequired),
    )
}

fun Float.ceilToInt() = ceil(this).toInt()

fun Blueprint.maxOreCost() = maxOf(
    oreRobotCost.ore, clayRobotCost.ore, obsidianRobotCost.ore, geodeRobotCost.ore
)

fun PuzzleLine.toBlueprint(): Blueprint {
    // Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
    // Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian
    val regex =
        Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.*")
    val matchResult = regex.matchEntire(this) ?: error("Invalid blueprint line: $this")

    return Blueprint(
        id = matchResult.groupValues[1].toInt(),
        oreRobotCost = OreCost(matchResult.groupValues[2].toInt()),
        clayRobotCost = OreCost(matchResult.groupValues[3].toInt()),
        obsidianRobotCost = OreClayCost(matchResult.groupValues[4].toInt(), matchResult.groupValues[5].toInt()),
        geodeRobotCost = OreObsidianCost(matchResult.groupValues[6].toInt(), matchResult.groupValues[7].toInt()),
    )
}


fun main() {
    val year = 2022
    val day = 19

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day19(input).partOne()

    println("Example 1: ${partOne(exampleInput)}")
}

