package days.y2019

import days.y2019.IntCodeComputer.IntCodeComputer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.hamcrest.core.Is.`is`

class IntCodeComputerTest {

    private val intCodeComputer = IntCodeComputer()

    @Test
    fun testDay2() {
        listOf(
            "1,0,0,0,99" to "2,0,0,0,99",
            "2,3,0,3,99" to "2,3,0,6,99",
            "1,1,1,4,99,5,6,0,99" to "30,1,1,4,2,5,6,0,99"
        ).forEach { (inputTape, outputTape) ->
            assertThat(intCodeComputer.run(inputTape), `is`(outputTape))
        }
    }


    @Test fun testDay5Tape() {
        listOf(
            "1002,4,3,4,33" to "1002,4,3,4,99",
        ).forEach { (inputTape, outputTape) ->
            assertThat(intCodeComputer.run(inputTape), `is`(outputTape))
        }
    }


}