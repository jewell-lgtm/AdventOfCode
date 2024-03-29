fun String.nonEmptyLines(): List<String> = lines().filter { it.isNotEmpty() }
fun MatchGroupCollection.getNamedInt(name: String) = this[name]?.value?.toInt() ?: error(name)
fun MatchGroupCollection.getNamedString(name: String) = this[name]?.value ?: error(name)

