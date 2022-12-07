package days.y2072


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day07 : Day(2022, 7) {
    var position = 1
    val rootDir = FSNode.from("/", null)
    var currNode = rootDir
    var lines = listOf<String>()

    override fun partOne(input: String): Any {
        parseInput(input)
        return rootDir.childNodesWhere { it.size() < 100000 }.sumOf { it.size() }
    }

    private fun parseInput(input: String) {
        lines = input.lines()
        while (position < lines.size) {
            if ("$ ls" == lines[position]) {
                position++
                readLsResult()
            } else if (lines[position].startsWith("$ cd")) {
                val (_, _, name) = lines[position].split(" ")
                changeDir(name)
                position++
            } else {
                error("Unknown command: ${lines[position]}")
            }
        }
    }

    private fun changeDir(name: String) {
        currNode = if (!name.startsWith("..")) {
            currNode.children.first { it.name == name }
        } else {
            currNode.parent!!
        }
    }

    private fun readLsResult() {
        while (position < lines.size && lines[position].isNotBlank()) {
            if (lines[position].startsWith("$")) {
                break
            } else if (lines[position].startsWith("dir")) {
                val dirName = lines[position].split(" ").last()
                currNode.children.add(FSNode.from(dirName, currNode))
            } else if (lines[position][0].isDigit()) {
                currNode.files.add(FSFile.from(lines[position]))
            } else error("Unknown line format: ${lines[position]}")
            position++
        }
    }


    override fun partTwo(input: String): Any {
        return -1
    }

    data class FSFile(val name: String, val size: Int) {
        companion object {
            fun from(line: String): FSFile {
                val parts = line.split(" ")
                return FSFile(parts[1], parts[0].toInt())
            }
        }
    }

    class FSNode(
        val name: String,
        val files: MutableList<FSFile>,
        val children: MutableList<FSNode>,
        val parent: FSNode?
    ) {
        fun size(): Int = files.sumOf { it.size } + children.sumOf { it.size() }
        fun childNodesWhere(function: (node: FSNode) -> Boolean): List<FSNode> {
            val result = mutableListOf<FSNode>()
            children.forEach { child ->
                if (function(child)) {
                    result.add(child)
                }
                result.addAll(child.childNodesWhere(function))
            }
            return result
        }

        companion object {
            fun from(name: String, parent: FSNode?): FSNode = FSNode(name, mutableListOf(), mutableListOf(), parent)
        }
    }
}


class Day07Test {
    private val exampleInput = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent()


    @Test
    fun testExampleOne() {
        assertThat(
            Day07().partOne(exampleInput).toString(), `is`(95437.toString())
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day07().partOne().toString(), `is`(1816))
    }

    @Test
    fun testPartTwo() {
        assertThat(Day07().partTwo(), `is`(2625))
    }
}
