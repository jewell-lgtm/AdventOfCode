package days.y2022.day19

import util.InputReader
import kotlin.math.max

fun partOne(input: String): Int {
    val blueprints = parseInput(input)
    val bestGeodes = blueprints.map { blueprint ->
        val memo = mutableMapOf<GameState, Int>()
        val initialState = GameState(19, 1)


        blueprint.maxGeodesFrom(memo, initialState)
    }

    return bestGeodes.max()
}

var i = -1
var hits = 0
fun Blueprint.maxGeodesFrom(memo: MutableMap<GameState, Int>, state: GameState): Int {
    i += 1
    if (i % 1000000 == 0) {
        println("$i ${memo.size}, hits: $hits")
    }
    if (memo[state] != null) {
        hits += 1
        memo[state]?.let { return it }
    }
    if (state.remainingMinutes == 0) {
        return state.amountGeodes
    }

    val possibilities = mutableListOf<GameState>()
    val nextState = state.wait().also { possibilities.add(it) }
    nextState.buyGeodeRobot(this)?.also {
        possibilities.add(it)
    }
    nextState.buyObsidianRobot(this)?.also { possibilities.add(it) }
    nextState.buyClayRobot(this)?.also { possibilities.add(it) }
    nextState.buyOreRobot(this)?.also { possibilities.add(it) }

    return possibilities.maxOf { newState ->
        maxGeodesFrom(memo, newState).also { memo[newState] = it }
    }
}


private fun GameState.buyOreRobot(blueprint: Blueprint): GameState? =
    blueprint.oreRobotCost.let { cost ->
        if (canAfford(cost)) copy(
            countOreRobots = countOreRobots + 1,
            amountOre = amountOre - cost.ore,
            amountClay = amountClay - cost.clay,
            amountObsidian = amountObsidian - cost.obsidian
        ) else null
    }

private fun GameState.buyClayRobot(blueprint: Blueprint): GameState? =
    blueprint.clayRobotCost.let { cost ->
        if (canAfford(cost)) copy(
            countClayRobots = countClayRobots + 1,
            amountOre = amountOre - cost.ore,
            amountClay = amountClay - cost.clay,
            amountObsidian = amountObsidian - cost.obsidian
        ) else null
    }

private fun GameState.buyObsidianRobot(blueprint: Blueprint): GameState? =
    blueprint.obsidianRobotCost.let { cost ->
        if (canAfford(cost)) copy(
            countObsidianRobots = countObsidianRobots + 1,
            amountOre = amountOre - cost.ore,
            amountClay = amountClay - cost.clay,
            amountObsidian = amountObsidian - cost.obsidian
        ) else null
    }

private fun GameState.buyGeodeRobot(blueprint: Blueprint): GameState? =
    blueprint.geodeRobotCost.let { cost ->
        if (canAfford(cost)) copy(
            countGeodeRobots = countGeodeRobots + 1,
            amountOre = amountOre - cost.ore,
            amountClay = amountClay - cost.clay,
            amountObsidian = amountObsidian - cost.obsidian
        ) else null
    }


private fun GameState.canAfford(cost: RobotCost): Boolean =
    amountOre >= cost.ore && amountClay >= cost.clay && amountObsidian >= cost.obsidian

private fun GameState.wait(): GameState = copy(
    remainingMinutes = remainingMinutes - 1,
    amountOre = (amountOre + countOreRobots),
    amountClay = (amountClay + countClayRobots),
    amountObsidian = (amountObsidian + countObsidianRobots)
)

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
    val id: Int, val oreRobotCost: RobotCost, val clayRobotCost: RobotCost,
    val obsidianRobotCost: RobotCost, val geodeRobotCost: RobotCost
) {
    companion object {
        fun from(line: String): Blueprint {
            val match =
                //            Blueprint 2:                  Each ore robot costs 2 ore.                Each clay robot costs 3 ore.                 Each obsidian robot costs 3 ore                        and 8 clay.                         Each geode robot costs 3                     ore and 12                         obsidian
                Regex("Blueprint (?<blueprint>\\d+): Each ore robot costs (?<oreCost>\\d+) ore. Each clay robot costs (?<clayCost>\\d+) ore. Each obsidian robot costs (?<obsidianOreCost>\\d+) ore and (?<obsidianClayCost>\\d+) clay. Each geode robot costs (?<geodeOreCost>\\d+) ore and (?<geodeObsidianCost>\\d+) obsidian")
                    .find(line)
                    ?.groups ?: error("Did not match: $line")


            val blueprint = match.getInt("blueprint")
            val oreCost = match.getInt("oreCost")
            val clayCost = match.getInt("clayCost")
            val obsidianOreCost = match.getInt("obsidianOreCost")
            val obsidianClayCost = match.getInt("obsidianClayCost")
            val geodeOreCost = match.getInt("geodeOreCost")
            val geodeObsidianCost = match.getInt("geodeObsidianCost")

            return Blueprint(
                id = blueprint,
                oreRobotCost = RobotCost(oreCost),
                clayRobotCost = RobotCost(clayCost),
                obsidianRobotCost = RobotCost(obsidianOreCost, clay = obsidianClayCost),
                geodeRobotCost = RobotCost(geodeOreCost, obsidian = geodeObsidianCost)
            )
        }

        private fun MatchGroupCollection.getInt(name: String) = this[name]?.value?.toInt() ?: error(name)
    }
}


fun parseInput(input: String): List<Blueprint> = input.nonEmptyLines().map(Blueprint::from)

private fun String.nonEmptyLines(): List<String> = lines().filter { it.isNotEmpty() }

fun main(args: Array<String>) {
    println("Example One: ${partOne(InputReader.getExample(2022, 19))}")
}
