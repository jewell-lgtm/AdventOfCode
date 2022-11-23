package days

import util.InputReader

abstract class Day(yearNumber: Int, dayNumber: Int) {
    fun partOne(): Any = partOne(inputString)
    fun partTwo(): Any = partTwo(inputString)

    abstract fun partOne(input: String): Any
    abstract fun partTwo(input: String): Any

    protected val inputString: String by lazy { InputReader.getInputAsString(yearNumber, dayNumber) }

}
