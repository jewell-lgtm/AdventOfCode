package util


fun timeIt(label: String = "Time taken", function: () -> Unit) {
    val start = System.currentTimeMillis()
    function()
    val end = System.currentTimeMillis()
    println("$label: ${end - start}ms")
}

