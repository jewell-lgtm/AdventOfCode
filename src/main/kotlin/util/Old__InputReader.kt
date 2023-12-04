@file:Suppress("ClassName")

package util

import java.io.File

object InputReader {
    fun getExample(year: Int, day: Int, filename: String = "example"): String {
        return fromResources(year, day, filename).readText().trim()
    }

    fun getExampleLines(year: Int, day: Int, filename: String = "example"): List<String> {
        return fromResources(year, day, filename).readLines().filter { it.isNotEmpty() }
    }

    fun getInput(year: Int, day: Int): String {
        return fromResources(year, day, "input").readText().trim()
    }

    fun getInputLines(year: Int, day: Int): List<String> {
        return fromResources(year, day, "input").readLines().filter { it.isNotEmpty() }
    }

    private fun fromResources(year: Int, day: Int, filename: String): File {
        val name = "$year/day_${day.toString().padStart(2, '0')}/$filename.txt"
        return File(javaClass.classLoader.getResource(name)?.toURI() ?: error("Input file $name not found"))
    }

}

object Old__InputReader {

    fun getInputAsString(year: Int, day: Int): String {
        return fromResources(year, day).readText()
    }

    fun getInputAsList(year: Int, day: Int): List<String> {
        return fromResources(year, day).readLines()
    }

    private fun fromResources(year: Int, day: Int): File {
        val name = "$year/input_day_$day.txt"
        return File(javaClass.classLoader.getResource(name)?.toURI() ?: error("Input file $name not found"))
    }
}
