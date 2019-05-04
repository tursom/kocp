package cn.tursom

import org.junit.Test
import kotlin.math.sqrt


class PrimeNumberTest {
	@Test
	fun testPrimeNumber() {
		println(PrimeNumber.biggestNumber)
		println(PrimeNumber.biggestNumber shr 4)
		PrimeNumber.calc(10000)
		println(PrimeNumber)
		println()
		assert(PrimeNumber[997])
		assert(!PrimeNumber[999])
		PrimeNumber.untilIterator(10000).forEach { print("$it ") }
		println()
	}

	@Test
	fun testSave() {
		println("${System.currentTimeMillis()}: test begin")
		PrimeNumber.calc(sqrt(Long.MAX_VALUE.toDouble()).toLong())
		println("${System.currentTimeMillis()}: result calculated")
//		File("prime").outputStream().use(PrimeNumber::save)
//		println("${System.currentTimeMillis()}: prime saved")
//		GZIPOutputStream(File("gzipPrime").outputStream()).use(PrimeNumber::save)
//		println("${System.currentTimeMillis()}: gzip prime saved")
//		GZIPInputStream(File("gzipPrime").inputStream()).use(PrimeNumber::load)
//		println("${System.currentTimeMillis()}: gzip prime loaded")
//		println(PrimeNumber[3])
//		println(PrimeNumber[5])
//		println(PrimeNumber[7])
//		println(PrimeNumber[9])
//		println(PrimeNumber)
	}

	@Test
	fun testDecomposition() {
		for (i in 1L..100L) {
			val sb = StringBuilder()
			sb.append("$i =")
			for (s in PrimeNumber.decomposition(i)) {
				sb.append(" $s *")
			}
			sb.deleteCharAt(sb.length - 1)
			sb.deleteCharAt(sb.length - 1)
			println(sb)
		}
	}

	@Test
	fun testCalcTime() {
		println(time {
			PrimeNumber.calc(20_0000_0000)
		})
		println(PrimeNumber)
	}


	@Test
	fun testBigNumber() {
		println("using time: ${time {
			println(PrimeNumber[1000000000000001])
		}}")
	}

	fun time(action: () -> Unit): Long {
		val t1 = System.currentTimeMillis()
		action()
		val t2 = System.currentTimeMillis()
		return t2 - t1
	}
}
