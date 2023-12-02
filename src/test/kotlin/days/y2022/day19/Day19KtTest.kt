package days.y2022.day19

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class Day19KtTest {


    @Nested
    inner class BuyingOreRobots {

        @Test
        fun `Can't buy a robot if ore per turn is 0`() {
            val blueprint =
                Blueprint(id = 1, oreRobotCost = RobotCost(1, 0, 0), clayRobotCost = RobotCost(100, 0, 0)).copy(
                    oreRobotCost = RobotCost(1, 0, 0)
                )
            val state = GameState(
                remainingMinutes = 100,
                amountOre = 0,
                countOreRobots = 0
            )
            assertNull(state.eventuallyBuyOreRobot(blueprint))
        }

        @Test
        fun `Can buy a robot in 1 turn if enough ore`() {
            val blueprint = Blueprint(1, oreRobotCost = RobotCost(1, 0, 0), clayRobotCost = RobotCost(10))
            val state = GameState(
                remainingMinutes = 100,
                amountOre = 10,
                amountClay = 0,
                amountObsidian = 0,
                amountGeodes = 0,
                countOreRobots = 0,
                countClayRobots = 1,
                countObsidianRobots = 1,
                countGeodeRobots = 1
            )
            val expected = GameState(
                remainingMinutes = 99,
                amountOre = 9,
                amountClay = 1,
                amountObsidian = 1,
                amountGeodes = 1,
                countOreRobots = 1,
                countClayRobots = 1,
                countObsidianRobots = 1,
                countGeodeRobots = 1
            )
            assertEquals(expected, state.eventuallyBuyOreRobot(blueprint))
        }

        @Test
        fun `Can buy a robot in multiple turns if ore per turn (exact multiple)`() {
            val blueprint = Blueprint(1, oreRobotCost = RobotCost(5), clayRobotCost = RobotCost(10))
            val state = GameState(
                remainingMinutes = 100,
                amountOre = 0,
                countOreRobots = 1
            )
            val expected = GameState(
                remainingMinutes = 95,
                amountOre = 0,
                countOreRobots = 2,
            )
            assertEquals(expected, state.eventuallyBuyOreRobot(blueprint))
        }

        @Test
        fun `Can buy a robot in multiple turns (with remainder)`() {
            val blueprint = Blueprint(1, oreRobotCost = RobotCost(5), clayRobotCost = RobotCost(10))
            val state = GameState(
                remainingMinutes = 100,
                amountOre = 2,
                countOreRobots = 2
            )
            // 2 ore per turn, starting with 2 ore, need 3 ore to buy a robot
            val expected = GameState(
                remainingMinutes = 98,
                amountOre = 1,
                countOreRobots = 3,
            )
            assertEquals(expected, state.eventuallyBuyOreRobot(blueprint))
        }

        @Test
        fun `As an optimisation, doesn't let you buy more ore robots than you could need per turn`() {
            val blueprint = Blueprint(1, oreRobotCost = RobotCost(5), clayRobotCost = RobotCost(10))
            val state = GameState(
                remainingMinutes = 100,
                amountOre = 0,
                countOreRobots = 10
            )
            assertNull(state.eventuallyBuyOreRobot(blueprint))
        }
    }

}
