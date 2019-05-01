package cn.tursom.BitArray

import cn.tursom.bitarray.FileBitArray
import org.junit.Test

class FileBitArrayTest {
	@Test
	fun test() {
		val fba = FileBitArray("fbat.bin")
		fba.resize(2000)
		println(fba[0])
		fba.up(0)
		fba.up(2000)
		println(fba[2000])
		fba.up(2000)
		println(fba[2000])
		println(fba[0])
	}
}