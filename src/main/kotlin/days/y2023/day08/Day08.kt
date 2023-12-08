package days.y2023.day08

import java.math.BigInteger
import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day08(val input: PuzzleInput) {
    val nodes = input.drop(2).toNodes()

    fun partOne(): Int {
        val lr = input.first().toLR()
        var currNode = nodes["AAA"]!!
        var steps = 0
        while (currNode.key != "ZZZ") {
            steps += 1
            val next = lr.next()
            val nextNode = when (next) {
                Direction.LEFT -> nodes[currNode.left]!!
                Direction.RIGHT -> nodes[currNode.right]!!
            }
            currNode = nextNode
        }
        return steps
    }

    fun pathLength(start: Node): Int {
        var length = 0
        var currNode = start
        val lr = input.first().toLR()

        while (!currNode.key.endsWith("Z")) {
            length += 1
            val direction = lr.next()
            currNode = when (direction) {
                Direction.LEFT -> nodes[currNode.left]!!
                Direction.RIGHT -> nodes[currNode.right]!!
            }
        }

        return length
    }

    fun partTwo(): BigInteger {

        val nodes = nodes.filter { it.key.endsWith("A") }.values.toList()
        val periodLengths = nodes.map { pathLength(it) }.map { it.toBigInteger() }

        println(periodLengths)

        return periodLengths.leastCommonMultiple()
    }

}

private fun List<BigInteger>.leastCommonMultiple(): BigInteger {
    var lcm = this[0]
    for (i in 1 until this.size) {
        lcm = lcm.multiply(this[i]).divide(lcm.gcd(this[i]))
    }
    return lcm
}

enum class Direction {
    LEFT, RIGHT
}

class NextLeftRight(
    private val directions: List<Direction>,
) {
    var i = -1
    private var realI = 0L
    fun next(): Direction {
        i = (i + 1) % directions.size
        realI += 1L
        if (realI % 100000000L == 0L) {
            println("We have taken $realI turns")
        }
        return directions[i]
    }
}

fun String.toLR(): NextLeftRight {
    val split = this.split("")
    return NextLeftRight(split.filter { it.isNotBlank() }.map {
        when (it) {
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            else -> error("Unknown direction $it")
        }
    })
}

data class Node(
    val key: String,
    val left: String,
    val right: String
)



fun List<String>.toNodes(): Map<String, Node> {
    val nodes = mutableMapOf<String, Node>()
    this.forEach { line ->
        val split = line.split(" = ")
        val name = split[0]
        val (left, right) = split[1].removeSurrounding("(", ")").split(", ")
        nodes[name] = Node(name, left, right)
    }
    return nodes
}

fun main() {
    val year = 2023
    val day = 8

    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)
    val exampleInput2: PuzzleInput = InputReader.getExampleLines(year, day, "example2")

    fun partOne(input: PuzzleInput) = Day08(input).partOne()
    fun partTwo(input: PuzzleInput) = Day08(input).partTwo()


    println("Example 1: ${partOne(exampleInput)}")
    println("Part 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput2)}")
    println("Part 2: ${partTwo(puzzleInput)}")
}

