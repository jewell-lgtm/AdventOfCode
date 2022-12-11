package util

import java.io.File

object InputReader {

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
