import kotlin.math.min
import kotlin.time.measureTimedValue

fun main()
{
    val numbers = listOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9
    )

    fun part1(input: List<String>) = input
        .map { it.toCharArray().filter { char -> char.isDigit() } }
        .sumOf {
            String(charArrayOf(it.first(), it.last())).toInt()
        }

    fun part2(input: List<String>) = input
        .sumOf { word ->
            var finalString = ""
            word.forEachIndexed { index, char ->
                if (char.isDigit())
                {
                    finalString += char
                    return@forEachIndexed
                }

                for (number in numbers)
                {
                    val embedded = word.substring(
                        index,
                        min(index + number.first.length, word.length)
                    )

                    if (embedded == number.first)
                    {
                        finalString += number.second
                        return@forEachIndexed
                    }
                }
            }

            "${finalString.first()}${finalString.last()}".toInt()
        }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day1", "test")
    check(part2(testInput).println() == 281)

    val input = readInput("Day1", "input")
    measureTimedValue { part1(input) }.println()
    measureTimedValue { part2(input) }.println()
}
