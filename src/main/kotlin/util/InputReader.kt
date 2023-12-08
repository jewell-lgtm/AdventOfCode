@file:Suppress("ClassName")

package util

import java.io.File

object InputReader {
    fun getExample(year: Int, day: Int, filename: String = "example"): String =
        getFileWithName(year, day, filename).readText().trim()

    fun getExampleLines(year: Int, day: Int, filename: String = "example"): List<String> =
        getFileWithName(year, day, filename).readLines().dropLastWhile { it.isEmpty() }

    fun getPuzzleInput(year: Int, day: Int): String = getFileWithName(year, day, "input").readText().trim()

    fun getPuzzleLines(year: Int, day: Int): List<String> =
        getFileWithName(year, day, "input").readLines().dropLastWhile { it.isEmpty() }

    private fun getFileWithName(year: Int, day: Int, filename: String): File =
        getResourceFile("$year/day_${day.toString().padStart(2, '0')}/$filename.txt")

    private fun getResourceFile(name: String): File =
        File(javaClass.classLoader.getResource(name)?.toURI() ?: error("Input file $name not found"))

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
