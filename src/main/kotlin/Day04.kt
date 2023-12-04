import kotlin.math.pow
import kotlin.time.measureTimedValue

fun main()
{
    data class Card(
        val cardID: Int,
        val winningNumbers: Set<Int>,
        val revealedNumbers: List<Int>
    )

    fun List<String>.parseIntoCardList() = map {
        val cardLabel = it.indexOf(':')
        val cardID = it.substring(0..cardLabel)

        val parsedCardID = unsignedNumberExtractor
            .find(cardID)
            ?.value?.toInt()
            ?: throw IllegalStateException(
                "Card number has no number"
            )

        val numberListDelimiter = it.indexOf('|')

        val winningNumbers = it.substring(cardLabel + 2..numberListDelimiter - 2)
        val revealedNumbers = it.substring(numberListDelimiter + 2)

        fun String.extractNumbers() = unsignedNumberExtractor.findAll(this)
            .toList()
            .map { result ->
                result.value.toInt()
            }

        parsedCardID to Card(
            cardID = parsedCardID,
            winningNumbers = winningNumbers.extractNumbers().toSet(),
            revealedNumbers = revealedNumbers.extractNumbers()
        )
    }.associate { it }

    fun part1(input: List<String>) = input.parseIntoCardList()
        .map {
            it.value.revealedNumbers.intersect(it.value.winningNumbers)
        }
        .filter { it.isNotEmpty() }
        .sumOf {
            if (it.size == 1)
            {
                return@sumOf 1
            }

            return@sumOf 2.0.pow(it.size - 1).toInt()
        }

    val mappings = mutableMapOf<Int, Int>()
    fun part2(input: List<String>) = input.parseIntoCardList()
        .map {
            it.value to it.value.revealedNumbers
                .intersect(it.value.winningNumbers)
        }
        .forEach {
            val instances = mappings.computeIfAbsent(it.first.cardID) { 1 }
            if (it.second.isEmpty())
            {
                return@forEach
            }

            for (nextCard in 0..<it.second.size)
            {
                mappings.compute(
                    it.first.cardID + nextCard + 1
                ) { _, value ->
                    (value ?: 1) + instances
                }
            }
        }
        .run { mappings.values.sum() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day4", "test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day4", "input")
    measureTimedValue { part1(input) }.println()
    measureTimedValue { part2(input) }.println()
}
