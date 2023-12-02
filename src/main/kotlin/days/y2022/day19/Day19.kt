package days.y2022.day19

import getNamedInt
import nonEmptyLines
import util.InputReader
import kotlin.math.ceil

fun partOne(input: String): Int {
    val blueprints = parseInput(input)
    val bestGeodes = blueprints.map { blueprint ->
        val memo = mutableMapOf<GameState, Int>()
        val initialState = GameState(24, 1)
        maxGeodes(memo, blueprint, initialState)
    }
    println("bestGeodes: $bestGeodes")
    return bestGeodes.max()
}

var comparisons = -1
fun maxGeodes(memo: MutableMap<GameState, Int>, blueprint: Blueprint, state: GameState): Int {
    comparisons += 1
    if (comparisons % 10000000 == 0) {
        println("$comparisons:${memo.size}")
    }
    if (memo[state] != null) {
        return memo[state]!!
    }
    if (state.remainingMinutes == 0) {
        return state.amountGeodes.also { memo[state] = it }
    }
    val possibilities = mutableListOf<GameState>()
    state.eventuallyBuyOreRobot(blueprint)?.let { possibilities.add(it) }
    state.eventuallyBuyClayRobot(blueprint)?.let { possibilities.add(it) }
    state.eventuallyBuyObsidianRobot(blueprint)?.let { possibilities.add(it) }
    state.eventuallyBuyGeodesRobot(blueprint)?.let { possibilities.add(it) }

    if (possibilities.isEmpty()) {
        val moreGeodes = state.amountGeodes + state.remainingMinutes * state.countGeodeRobots
        return moreGeodes.also { memo[state] = it }
    }

    return possibilities.maxOf { maxGeodes(memo, blueprint, it) }.also { memo[state] = it }
}

fun GameState.eventuallyBuyOreRobot(blueprint: Blueprint): GameState? {
    if (countOreRobots >= blueprint.maxOreCost) return null
    if (remainingMinutes > 0 && amountOre > blueprint.oreRobotCost.ore) return copy(
        remainingMinutes = remainingMinutes - 1,
        countOreRobots = countOreRobots + 1,
        amountOre = amountOre + countOreRobots - blueprint.oreRobotCost.ore,
        amountClay = amountClay + countClayRobots,
        amountObsidian = amountObsidian + countObsidianRobots,
        amountGeodes = amountGeodes + countGeodeRobots
    )
    if (countOreRobots == 0) return null
    val turnsToBuy = ((blueprint.oreRobotCost.ore - amountOre).toDouble() / countOreRobots.toDouble()).ceilToInt()
    if (turnsToBuy > remainingMinutes) return null
    return copy(
        remainingMinutes = remainingMinutes - turnsToBuy,
        countOreRobots = countOreRobots + 1,
        amountOre = amountOre + turnsToBuy * countOreRobots - blueprint.oreRobotCost.ore,
        amountClay = amountClay + turnsToBuy * countClayRobots,
        amountObsidian = amountObsidian + turnsToBuy * countObsidianRobots,
        amountGeodes = amountGeodes + turnsToBuy * countGeodeRobots
    )
}

// clay robots are also bought with ore
fun GameState.eventuallyBuyClayRobot(blueprint: Blueprint): GameState? {
    if (countClayRobots >= blueprint.maxClayCost) return null
    if (remainingMinutes > 0 && amountOre > blueprint.clayRobotCost.ore) return copy(
        remainingMinutes = remainingMinutes - 1,
        countClayRobots = countClayRobots + 1,
        amountOre = amountOre + countOreRobots - blueprint.clayRobotCost.ore,
        amountClay = amountClay + countClayRobots,
        amountObsidian = amountObsidian + countObsidianRobots,
        amountGeodes = amountGeodes + countGeodeRobots
    )
    if (countOreRobots == 0) return null
    val turnsToBuy = ((blueprint.clayRobotCost.ore - amountOre).toDouble() / countOreRobots.toDouble()).ceilToInt()
    if (turnsToBuy > remainingMinutes) return null
    return copy(
        remainingMinutes = remainingMinutes - turnsToBuy,
        countClayRobots = countClayRobots + 1,
        amountOre = amountOre + (turnsToBuy * countOreRobots) - blueprint.clayRobotCost.ore,
        amountClay = amountClay + (turnsToBuy * countClayRobots),
        amountObsidian = amountObsidian + (turnsToBuy * countObsidianRobots),
        amountGeodes = amountGeodes + (turnsToBuy * countGeodeRobots),
    )
}

// obsidian robots are bought with ore and clay
fun GameState.eventuallyBuyObsidianRobot(blueprint: Blueprint): GameState? {
    if (countObsidianRobots >= blueprint.maxObsidianCost) return null
    if (remainingMinutes > 0 && amountOre > blueprint.obsidianRobotCost.ore && amountClay > blueprint.obsidianRobotCost.clay) return copy(
        remainingMinutes = remainingMinutes - 1,
        countObsidianRobots = countObsidianRobots + 1,
        amountOre = amountOre + countOreRobots - blueprint.obsidianRobotCost.ore,
        amountClay = amountClay + countClayRobots - blueprint.obsidianRobotCost.clay,
        amountObsidian = amountObsidian + countObsidianRobots,
        amountGeodes = amountGeodes + countGeodeRobots
    )
    if (countOreRobots == 0 || countClayRobots == 0) return null
    val oreTurnsToBuy =
        ((blueprint.obsidianRobotCost.ore - amountOre).toDouble() / countOreRobots.toDouble()).ceilToInt()
    val clayTurnsToBuy =
        ((blueprint.obsidianRobotCost.clay - amountClay).toDouble() / countClayRobots.toDouble()).ceilToInt()
    val turnsToBuy = maxOf(oreTurnsToBuy, clayTurnsToBuy)
    if (turnsToBuy > remainingMinutes) return null
    return copy(
        remainingMinutes = remainingMinutes - turnsToBuy,
        countObsidianRobots = countObsidianRobots + 1,
        amountOre = amountOre + turnsToBuy * countOreRobots - blueprint.obsidianRobotCost.ore,
        amountClay = amountClay + turnsToBuy * countClayRobots - blueprint.obsidianRobotCost.clay,
        amountObsidian = amountObsidian + turnsToBuy * countObsidianRobots,
        amountGeodes = amountGeodes + turnsToBuy * countGeodeRobots
    )
}

// geodes are bought with ore and obsidian
fun GameState.eventuallyBuyGeodesRobot(blueprint: Blueprint): GameState? {
    // never need too many geodes
    if (remainingMinutes > 0 && amountOre > blueprint.geodeRobotCost.ore && amountObsidian > blueprint.geodeRobotCost.obsidian) return copy(
        remainingMinutes = remainingMinutes - 1,
        countGeodeRobots = countGeodeRobots + 1,
        amountOre = amountOre + countOreRobots - blueprint.geodeRobotCost.ore,
        amountClay = amountClay + countClayRobots,
        amountObsidian = amountObsidian + countObsidianRobots - blueprint.geodeRobotCost.obsidian,
        amountGeodes = amountGeodes + countGeodeRobots
    )
    if (countOreRobots == 0 || countObsidianRobots == 0) return null
    val oreTurnsToBuy = ((blueprint.geodeRobotCost.ore - amountOre).toDouble() / countOreRobots.toDouble()).ceilToInt()
    val obsidianTurnsToBuy =
        ((blueprint.geodeRobotCost.obsidian - amountObsidian).toDouble() / countObsidianRobots.toDouble()).ceilToInt()
    val turnsToBuy = maxOf(oreTurnsToBuy, obsidianTurnsToBuy)
    if (turnsToBuy > remainingMinutes) return null
    return copy(
        remainingMinutes = remainingMinutes - turnsToBuy,
        countGeodeRobots = countGeodeRobots + 1,
        amountOre = amountOre + turnsToBuy * countOreRobots - blueprint.geodeRobotCost.ore,
        amountClay = amountClay + turnsToBuy * countClayRobots,
        amountObsidian = amountObsidian + turnsToBuy * countObsidianRobots - blueprint.geodeRobotCost.obsidian,
        amountGeodes = amountGeodes + turnsToBuy * countGeodeRobots
    )
}

private fun Double.ceilToInt(): Int = ceil(this).toInt()


data class GameState(
    val remainingMinutes: Int,
    val countOreRobots: Int,
    val countClayRobots: Int = 0,
    val countObsidianRobots: Int = 0,
    val countGeodeRobots: Int = 0,
    val amountOre: Int = 0,
    val amountClay: Int = 0,
    val amountObsidian: Int = 0,
    val amountGeodes: Int = 0
)

data class RobotCost(val ore: Int, val clay: Int = 0, val obsidian: Int = 0)
data class Blueprint(
    val id: Int,
    val oreRobotCost: RobotCost = RobotCost(1),
    val clayRobotCost: RobotCost = RobotCost(1),
    val obsidianRobotCost: RobotCost = RobotCost(0, 1),
    val geodeRobotCost: RobotCost = RobotCost(0, 0, 1)
) {
    val maxOreCost = maxOf(oreRobotCost.ore, clayRobotCost.ore, obsidianRobotCost.ore, geodeRobotCost.ore)
    val maxClayCost = maxOf(oreRobotCost.clay, clayRobotCost.clay, obsidianRobotCost.clay, geodeRobotCost.clay)
    val maxObsidianCost =
        maxOf(oreRobotCost.obsidian, clayRobotCost.obsidian, obsidianRobotCost.obsidian, geodeRobotCost.obsidian)

    companion object {
        fun from(line: String): Blueprint {
            val match =
                //            Blueprint 2:                  Each ore robot costs 2 ore.                Each clay robot costs 3 ore.                 Each obsidian robot costs 3 ore                        and 8 clay.                         Each geode robot costs 3                     ore and 12                         obsidian
                Regex("Blueprint (?<blueprint>\\d+): Each ore robot costs (?<oreCost>\\d+) ore. Each clay robot costs (?<clayCost>\\d+) ore. Each obsidian robot costs (?<obsidianOreCost>\\d+) ore and (?<obsidianClayCost>\\d+) clay. Each geode robot costs (?<geodeOreCost>\\d+) ore and (?<geodeObsidianCost>\\d+) obsidian")
                    .find(line)
                    ?.groups ?: error("Did not match: $line")


            val blueprint = match.getNamedInt("blueprint")
            val oreCost = match.getNamedInt("oreCost")
            val clayCost = match.getNamedInt("clayCost")
            val obsidianOreCost = match.getNamedInt("obsidianOreCost")
            val obsidianClayCost = match.getNamedInt("obsidianClayCost")
            val geodeOreCost = match.getNamedInt("geodeOreCost")
            val geodeObsidianCost = match.getNamedInt("geodeObsidianCost")

            return Blueprint(
                id = blueprint,
                oreRobotCost = RobotCost(oreCost),
                clayRobotCost = RobotCost(clayCost),
                obsidianRobotCost = RobotCost(obsidianOreCost, clay = obsidianClayCost),
                geodeRobotCost = RobotCost(geodeOreCost, obsidian = geodeObsidianCost)
            )
        }


    }
}


fun parseInput(input: String): List<Blueprint> = input.nonEmptyLines().map(Blueprint::from)


fun main() {
    println("Example One: ${partOne(InputReader.getExample(2022, 19))}")
}
