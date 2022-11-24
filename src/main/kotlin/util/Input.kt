package util

fun String.integers(s: String): List<Int> = split(s).map { it.toInt() }