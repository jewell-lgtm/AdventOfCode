package days.y2022


import days.Day
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day07 : Day(2022, 7) {
    private lateinit var rootDir: FSNode

    override fun partOne(input: String): Any {
        parseInput(input)
        return rootDir.childNodesWhere { it.size() < 100000 }.sumOf { it.size() }
    }

    override fun partTwo(input: String): Any {
        parseInput(input)
        val unusedSpace = (70000000 - rootDir.size())
        val neededSpace = 30000000 - unusedSpace

        val dirsToDelete = rootDir.childNodesWhere { it.size() >= neededSpace }
        val dirToDelete = dirsToDelete.minBy { it.size() }
        return dirToDelete.size()
    }

    private fun parseInput(input: String) {
        rootDir = FSNode.from("..", null)
        val lines = input.lines().drop(1) // first command is $ cd /
        var currNode = rootDir

        for (line in lines) {
            if (line == "$ ls") {
                continue
            }
            if (line.startsWith("$ cd ")) {
                val dirName = line.split(" ").last()
                currNode = if (dirName == ".." ) {
                    currNode.parent!!
                } else {
                    currNode.child(dirName)
                }
                continue
            }
            if (line.startsWith("dir ")) {
                val dirName = line.split(" ").last()
                currNode.children.add(FSNode.from(dirName, currNode))
                continue
            }
            if (line[0].isDigit()) {
                val (size, name) = line.split(" ")
                currNode.files.add(FSFile(name, size.toInt()))
                continue
            }
            error("Unknown format: $line")
        }
    }

    data class FSFile(val name: String, val size: Int)

    data class FSNode(
        val name: String,
        val files: MutableSet<FSFile>,
        val children: MutableSet<FSNode>,
        val parent: FSNode?
    ) {
        fun size(): Int = files.sumOf { it.size } + children.sumOf { it.size() }
        fun childNodesWhere(function: (node: FSNode) -> Boolean): Set<FSNode> {
            val result = mutableSetOf<FSNode>()
            children.forEach { child ->
                if (function(child)) {
                    result.add(child)
                }
                result.addAll(child.childNodesWhere(function))
            }
            return result
        }

        fun child(dirName: String): FSNode = this.children.find { it.name == dirName }!!

        companion object {
            fun from(name: String, parent: FSNode?): FSNode = FSNode(name, mutableSetOf(), mutableSetOf(), parent)
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
            Day07().partOne(exampleInput), `is`(95437)
        )
    }

    @Test
    fun testPartOne() {
        assertThat(Day07().partOne(), `is`(1915606))
    }

    @Test
    fun testExampleTwo() {
        assertThat(
            Day07().partTwo(exampleInput).toString(), `is`(24933642.toString())
        )
    }

    @Test
    fun testPartTwo() {
        assertThat(Day07().partTwo(), `is`(5025657))
    }
}
