package cn.tursom

import org.junit.Test
import kotlin.math.sqrt


class PrimeNumberTest {
	@Test
	fun testPrimeNumber() {
		println(PrimeNumber)
		println(PrimeNumber.biggestNumber)
		PrimeNumber.calc(10000)
		for (i in 1..10000) {
			if (PrimeNumber[i]) print("$i ")
		}
		println()
		println(PrimeNumber)
		assert(PrimeNumber[997])
		assert(!PrimeNumber[999])
		println(PrimeNumber[20000005])
		println(PrimeNumber)
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
}
