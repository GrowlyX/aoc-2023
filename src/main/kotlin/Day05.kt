fun main()
{
    data class Mapping(
        val destinationRangeStart: Int,
        val sourceRangeStart: Int,
        val rangeLength: Int
    )

    data class Stage(
        val from: String,
        val to: String,
        val mappings: MutableList<Mapping> = mutableListOf()
    )

    data class Almanac(
        val seeds: Set<Int>,
        val stages: List<Stage>
    )

    fun List<String>.parseIntoAlmanac() =
        with(this) {
            val mutableCopy = toMutableList()
            val seeds = mutableCopy.removeAt(0)
                .extractNumbers()
                .apply {
                    check(isNotEmpty())
                }
                .toSet()

            val stages = mutableListOf<Stage>()
            var currentStage: Stage? = null
            for (line in mutableCopy)
            {
                if (line.isBlank())
                {
                    if (currentStage != null)
                    {
                        stages += currentStage
                    }
                    continue
                }

                if (line.endsWith("map:"))
                {
                    val stageDelimiter = line.indexOf('-')
                    currentStage = Stage(
                        from = line.substring(
                            0..<stageDelimiter
                        ),
                        to = line.substring(
                            stageDelimiter + 4..<line.indexOf(' ')
                        )
                    )
                    continue
                }

                val numbers = line.extractNumbers()
                    .apply {
                        check(size == 3)
                    }

                val (destinationRangeStart, sourceRangeStart, rangeLength) =
                    Triple(
                        numbers[0], numbers[1], numbers[2]
                    )

                checkNotNull(currentStage).mappings += Mapping(
                    destinationRangeStart = destinationRangeStart,
                    sourceRangeStart = sourceRangeStart,
                    rangeLength = rangeLength
                )
            }

            Almanac(seeds = seeds, stages = stages.toList())
        }

    fun part1(input: List<String>): Int
    {
        input.parseIntoAlmanac().println()
        return 0
    }
    fun part2(input: List<String>) = 0

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day5", "test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    /*val input = readInput("Day5", "input")
    measureTimedValue { part1(input) }.println()
    measureTimedValue { part2(input) }.println()*/
}
