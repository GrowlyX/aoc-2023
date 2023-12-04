import java.lang.ref.WeakReference
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

val unsignedNumberExtractor = "[1-9]\\d*".toRegex()
val signedNumberExtractor = "-?[1-9]\\d*".toRegex()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, file: String) = Path("src/main/resources/$name/$file.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.println() = apply { println(this) }

data class CharPoint(
    val x: Int,
    val y: Int
)
{
    fun pointAt(plane2D: CharPlane2D) = plane2D.points[this] ?: '.'
}

data class CharPlane2D(
    val points: Map<CharPoint, Char>
)
{
    fun expand(region: Region2D, expandsBy: Int): Region2D
    {
        val minX = minOf(region.point1.x, region.point2.x) - expandsBy
        val minY = minOf(region.point1.y, region.point2.y) - expandsBy
        val maxX = maxOf(region.point1.x, region.point2.x) + expandsBy
        val maxY = maxOf(region.point1.y, region.point2.y) + expandsBy

        val expandedPoint1 = CharPoint(minX, minY)
        val expandedPoint2 = CharPoint(maxX, maxY)

        return Region2D(expandedPoint1, expandedPoint2, WeakReference(this))
    }

    fun bordering(region: Region2D) = expand(region, expandsBy = 1)
        .toList()
        .filterNot(region::coercesIn)
}

data class Region2D(
    val point1: CharPoint,
    val point2: CharPoint,
    val plane: WeakReference<CharPlane2D>
) : Iterable<CharPoint>
{
    private val minX = minOf(point1.x, point2.x)
    private val maxX = maxOf(point1.x, point2.x)
    private val minY = minOf(point1.y, point2.y)
    private val maxY = maxOf(point1.y, point2.y)

    private val xRange = minX..maxX
    private val yRange = minY..maxY

    fun coercesIn(point: CharPoint) = point.x in xRange && point.y in yRange

    override fun iterator() = iterator {
        for (x in xRange)
        {
            for (y in yRange)
            {
                yield(CharPoint(x, y))
            }
        }
    }
}
