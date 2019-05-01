package cn.tursom

import org.junit.Test

class PrimeNumberTest {
	@Test
	fun testPrimeNumber() {
		for (i in 1..1000) {
			if (PrimeNumber[i]) print("$i ")
		}
		println()
		assert(PrimeNumber[997])
		assert(!PrimeNumber[999])
	}
}