package days.y2019.Day05

import days.Day
import days.y2019.IntCodeComputer.IntCodeComputer
import util.integers

class Day05 : Day(2019, 5) {

    override fun partOne(input: String): Any {
        val intCodeComputer = IntCodeComputer()
        intCodeComputer.run(input.integers(","))
        return intCodeComputer.output.last()
    }

    override fun partTwo(input: String): Any {

        return -1
    }
}

