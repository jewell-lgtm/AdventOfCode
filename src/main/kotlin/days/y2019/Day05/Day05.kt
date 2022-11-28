package days.y2019.Day05

import days.Day
import days.y2019.IntCodeComputer.IntCodeComputer
import util.integers

class Day05 : Day(2019, 5) {

    override fun partOne(input: String): Any {
        val intCodeComputer = IntCodeComputer.from(input.integers(","), 1)
        intCodeComputer.compute()
        return intCodeComputer.output.last()
    }

    override fun partTwo(input: String): Any {
        val intCodeComputer = IntCodeComputer.from(input.integers(","), 5)
        intCodeComputer.compute()
        return intCodeComputer.output.last()
    }
}

