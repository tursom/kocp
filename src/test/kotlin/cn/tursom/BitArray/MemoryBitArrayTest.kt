package cn.tursom.BitArray

import cn.tursom.bitarray.MemoryBitArray
import org.junit.Test

class MemoryBitArrayTest {
	@Test
	fun testBitSet() {
		val bitSet = MemoryBitArray()
		bitSet.up(1)
		bitSet.down(2)
		assert(!bitSet[0])
		assert(bitSet[1])
		assert(!bitSet[2])
		assert(!bitSet[1000])
		assert(bitSet.maxIndex == 1023L)
	}

	@Test
	fun testTrueCount() {
		val bitSet = MemoryBitArray(10000)
		assert(bitSet.trueCount == 0L)
		bitSet.up(1)
		assert(bitSet.trueCount == 1L)
		bitSet.up(10000)
		assert(bitSet.trueCount == 2L)
	}
}