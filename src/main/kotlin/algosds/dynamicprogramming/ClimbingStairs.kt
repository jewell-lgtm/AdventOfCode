package algosds.dynamicprogramming

import util.timeIt
import kotlin.math.min
import kotlin.random.Random

// For a given staircase, the i-th step is assigned a non-negative cost indicated by a cost array.
// Once you pay the cost for a step, you can either climb one or two steps. Find the minimum cost to reach the top of the staircase.
// Your first step can either be the first or second step.


// e.g. cost = [10,15,30]

fun main() {
    timeIt {
        println(calculateCostOf(listOf(1, 1, 2, 3, 5, 8, 13, 10, 20, 30, 40, 1, 2, 3, 4)))
        println(calculateCostOf((0..9999).map { Random.nextInt(0, 9999) }))
    }
}

fun calculateCostOf(costs: List<Int>): Int {
    val memo = mutableMapOf<Int, Int>()

    fun List<Int>.costAt(index: Int): Int {
        if (index == this.size) return 0
        if (index < 0) return 0
        return this[index]
    }

    var comparisons = 0
    fun minCostTo(index: Int): Int {
        comparisons ++
        if (comparisons % 1000 == 0) {
            println("$comparisons Comparisons")
        }
        if (index < 2) return costs.costAt(index)
        return memo.getOrPut(index) {
            costs.costAt(index) + min(minCostTo(index - 2), minCostTo(index - 1))
        }
//        return costs.costAt(index) + min(minCostTo(index - 2), minCostTo(index - 1))
    }

    return minCostTo(costs.size)
}
