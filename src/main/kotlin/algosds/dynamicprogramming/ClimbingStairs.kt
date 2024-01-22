package algosds.dynamicprogramming

import util.timeIt
import kotlin.math.min
import kotlin.random.Random

// For a given staircase, the i-th step is assigned a non-negative cost indicated by a cost array.
// Once you pay the cost for a step, you can either climb one or two steps. Find the minimum cost to reach the top of the staircase.
// Your first step can either be the first or second step.


// e.g. cost = [10,15,30]

fun main() {
    val a = listOf(1, 1, 2, 3, 5, 8, 13, 10, 20, 30, 40, 1, 2, 3, 4)
    val b = (0..9999).map { Random.nextInt(0, 9999) }

    timeIt {
        println(calculateCostToRecursive(a))
        println(calculateCostToRecursive(b))
    }
    timeIt {
        println(calculateCostToIterative(a))
        println(calculateCostToIterative(b))
    }
    println(calculateCostToRecursive(a) == calculateCostToIterative(a))
    println(calculateCostToRecursive(b) == calculateCostToRecursive(b))
}

fun Int?.unwrap(): Int {
    if (this == null) error("Value should not be null")
    return this
}

interface LRUCache {
    operator fun set(key: Int, value: Int): Unit
    operator fun get(key: Int): Int // in this example require cache hit
}

fun getCache(capacity: Int): LRUCache {
    val map = mutableMapOf<Int, Int>()
    return object : LRUCache {
        override fun set(key: Int, value: Int) {
            if (map.size >= capacity) {
                val oldestKey = map.keys.iterator().next()
                map.remove(oldestKey)
            }
            map[key] = value
        }

        override fun get(key: Int): Int {
            val value = map[key].unwrap() // throw on cache miss
            // remove the key and reinsert to put it at the top
            map.remove(key)
            map[key] = value
            return value
        }
    }
}

fun calculateCostToIterative(costs: List<Int>): Int {
    return when (costs.size) {
        0 -> 0
        1 -> 0
        2 -> min(costs[0], costs[1])
        else -> (2 until costs.size)
            .fold(costs.getInitialCache()) { cache, index ->
                cache.apply {
                    val cost1 = get(index - 1)
                    val cost2 = get(index - 2)
                    val costThis = costs[index]
                    set(index, costThis + min(cost1, cost2))
                }
            }.let { cache -> min(cache[costs.size - 1], cache[costs.size - 2]) }
    }
}

fun List<Int>.getInitialCache() = getCache(3).also {
    it[0] = this[0]
    it[1] = this[1]
}

fun calculateCostToRecursive(costs: List<Int>): Int {
    val memo = mutableMapOf<Int, Int>()

    fun List<Int>.costAt(index: Int): Int {
        if (index == this.size) return 0
        if (index < 0) return 0
        return this[index]
    }

    var comparisons = 0
    fun minCostTo(index: Int): Int {
        comparisons++
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
