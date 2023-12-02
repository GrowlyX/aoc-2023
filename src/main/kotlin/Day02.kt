import kotlin.time.measureTimedValue

fun main()
{
    val nonNumeric = "[^0-9]".toRegex()
    val numeric = "[^a-z]".toRegex()

    val limits = mapOf("red" to 12, "green" to 13, "blue" to 14)

    fun List<String>.parseInto() =
        map {
            val gameID = it.indexOf(':')
            it.substring(0..<gameID) to it
                .substring(gameID + 2)
                .split("; ")
                .map { group ->
                    group.split(", ")
                }
        }
        .map {
            it.first.removePrefix("Game ").toInt() to
                it.second.map { group ->
                    group.map { dice ->
                        dice.replace(nonNumeric, "").toInt() to
                            dice.replace(numeric, "")
                    }
                }
        }

    fun part1(input: List<String>) = input.parseInto()
        .filter { mappings ->
            mappings.second.all { group ->
                group.none { it.first > limits[it.second]!! }
            }
        }
        .sumOf { it.first }

    fun part2(input: List<String>) = input.parseInto()
        .sumOf {
            val minimums = mutableMapOf<String, Int>()

            for (pair in it.second.flatten())
            {
                minimums.putIfAbsent(pair.second, pair.first)
                if (minimums[pair.second]!! < pair.first)
                {
                    minimums[pair.second] = pair.first
                }
            }

            minimums.values.reduce { acc, i -> acc * i }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day2", "test")
    check(part1(testInput).println() == 8)
    check(part2(testInput).println() == 2286)

    val input = readInput("Day2", "input")
    measureTimedValue { part1(input) }.println()
    measureTimedValue { part2(input) }.println()
}
