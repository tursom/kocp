package cn.tursom

import org.junit.Test

class BitSetTest {
	@Test
	fun testBitSet() {
		val bitSet = BitArray()
		bitSet.up(1)
		bitSet.down(2)
		assert(!bitSet[0])
		assert(bitSet[1])
		assert(!bitSet[2])
		assert(!bitSet[1000])
		assert(bitSet.size == 1023L)
	}
}