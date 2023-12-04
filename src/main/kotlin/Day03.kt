import java.lang.ref.WeakReference
import kotlin.time.measureTimedValue

fun main()
{
    data class Part(
        val region: Region2D,
        val value: Int
    )

    fun List<String>.mapIntoPlaneAndParts() = with(with(this) {
        val pointMappings = mutableMapOf<CharPoint, Char>()
        forEachIndexed { index, schematic ->
            schematic.forEachIndexed { charIndex, char ->
                pointMappings[CharPoint(index, charIndex)] = char
            }
        }

        CharPlane2D(points = pointMappings)
    }) {
        this to map { unsignedNumberExtractor.findAll(it).toList() }
            .mapIndexed { index, results ->
                results.map {
                    Part(
                        region = Region2D(
                            point1 = CharPoint(index, it.range.first),
                            point2 = CharPoint(index, it.range.last),
                            plane = WeakReference(this)
                        ),
                        value = it.value.toInt()
                    )
                }
            }
            .flatten()
    }

    fun part1(input: List<String>) = with(input.mapIntoPlaneAndParts()) {
        second
            .filterNot {
                val bordering = first.bordering(it.region)

                bordering.all { point ->
                    val value = point.pointAt(first)
                    value.isLetterOrDigit() || value == '.'
                }
            }
            .sumOf { it.value }
    }

    fun part2(input: List<String>) = with(input.mapIntoPlaneAndParts()) {
        val gears = mutableMapOf<CharPoint, List<Int>>()
        second.forEach {
            val bordering = first.bordering(it.region)
            bordering
                .filter { gear -> gear.pointAt(first) == '*' }
                .forEach { gear ->
                    gears.compute(gear) { _, parts -> (parts ?: listOf()) + it.value }
                }
        }

        gears.values
            .filter { it.size == 2 }
            .sumOf { it.first() * it.last() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day3", "test")
    println(part1(testInput).println())
    println(part2(testInput).println())

    val input = readInput("Day3", "input")
    measureTimedValue { part1(input) }.println()
    measureTimedValue { part2(input) }.println()
}
