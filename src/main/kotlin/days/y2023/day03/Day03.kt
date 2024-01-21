package days.y2023.day03

import util.InputReader


private class Day03(puzzleInput: String) {

    val grid = puzzleInput.lines().filter { it.isNotEmpty() }.map { line -> line.toCharArray().toList() }.toList()

    fun partOne(): Int {
        val foundNumbers = mutableListOf<Int>()
        val digits = mutableListOf<Char>()
        val positions = mutableListOf<Pair<Int, Int>>()
        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    digits.add(char)
                    positions.add(Pair(x, y))
                }

                if (!char.isDigit() || line.lastX == x) {
                    val hasSpecialNeighbor = positions.any { position ->
                        grid.adjacentTo(position).any { it.isSpecial() }
                    }
                    if (hasSpecialNeighbor) {
                        foundNumbers.add(digits.joinToString("").toInt())
                    }


                    digits.clear()
                    positions.clear()
                }

            }
        }
        println("Found numbers: $foundNumbers")
        return foundNumbers.sum()
    }

    fun partTwo(): Int {
        val foundNumbers = mutableMapOf<Set<Pair<Int, Int>>, Int>()
        val digits = mutableListOf<Char>()
        val positions = mutableListOf<Pair<Int, Int>>()
        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    digits.add(char)
                    positions.add(Pair(x, y))
                }
                if (!char.isDigit() || line.lastX == x) {
                    if (positions.isNotEmpty()) {
                        foundNumbers[positions.toSet()] = digits.joinToString("").toInt()
                    }

                    digits.clear()
                    positions.clear()
                }
            }
        }
        val gearRatios = mutableListOf<Pair<Int, Int>>()

        grid.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char.isSpecial('*')) {
                    val adjacent = grid.adjacentToCoords(Pair(x, y))
                    val numbersAdjacentToIt = foundNumbers.filter { (positions, _) ->
                        positions.intersect(adjacent).isNotEmpty()
                    }.values.toList()
                    if (numbersAdjacentToIt.size == 2) {
                        val (first, second) = numbersAdjacentToIt
                        gearRatios.add(Pair(first, second))
                    }
                }
            }
        }

        return gearRatios.sumOf { it.first * it.second }
    }
}


private fun Char.isSpecial(c: Char? = null): Boolean =
    if (c != null) this == c else this != ' ' && this != '.' && !isDigit()

private fun <E> List<List<E>>.adjacentTo(position: Pair<Int, Int>): Set<E> {
    val coords = adjacentToCoords(position)
    return coords.map { (x, y) -> this[y][x] }.toSet()
}

private fun <E> List<List<E>>.adjacentToCoords(position: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val (x, y) = position
    val validPositions = mutableListOf<Pair<Int, Int>>()
    for (dy in -1..1) {
        for (dx in -1..1) {
            if (dx == 0 && dy == 0) continue
            val newX = x + dx
            val newY = y + dy
            if (newX < 0 || newY < 0) continue
            if (newY >= size || newX >= this[newY].size) continue
            validPositions.add(Pair(newX, newY))
        }
    }
    return validPositions.toSet()
}


private val <E> List<E>.lastX: Int
    get() = this.size - 1

fun main() {
    println("Example 1: ${Day03(InputReader.getExample(2023, 3)).partOne()}")
    println("Part 1: ${Day03(InputReader.getPuzzle(2023, 3)).partOne()}")
    println("Example 2: ${Day03(InputReader.getExample(2023, 3)).partTwo()}")
    println("Part 2: ${Day03(InputReader.getPuzzle(2023, 3)).partTwo()}")
}
