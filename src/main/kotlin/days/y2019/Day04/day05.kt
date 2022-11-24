package days.y2019.Day04

import days.Day

public class Day04 : Day(2019, 4) {

    override fun partOne(input: String): Any {
        val (start, end) = input.split("-").map { it.toInt() }

        return (start..end).count { it.isValid() }
    }

    override fun partTwo(input: String): Any {
        val (start, end) = input.split("-").map { it.toInt() }

        return (start..end).filter { it.isValid() }.count { it.isValid2() }
    }
}


private fun Int.isValid2():Boolean = toString().isValid2()

private fun Int.isValid(): Boolean = toString().isValid()

fun String.isValid(): Boolean {
    if (length != 6) return false
    if (toCharArray().sorted().joinToString("") != this) return false
    return hasDoubleChar()
}

fun String.isValid2(): Boolean {
    val groupsOfChars = toCharArray().groupBy { it }
    return groupsOfChars.any { it.value.size == 2 }
}

private fun String.hasDoubleChar(): Boolean {
    var lastChar = ' '
    for (char in this) {
        if (char == lastChar) return true
        lastChar = char
    }
    return false
}

