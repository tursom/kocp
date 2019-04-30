package cn.tursom

import org.junit.Test

class BitSetTest {
	@Test
	fun testBitSet() {
		val bitSet = BitSet()
		bitSet.up(1)
		bitSet.down(2)
		println(bitSet[0])
		println(bitSet[1])
		println(bitSet[2])
		println(bitSet[1000])
		println(bitSet.size)
	}
}