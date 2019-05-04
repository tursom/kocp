package cn.tursom.bitarray

interface ReadableBitArray {
	val maxIndex: Long
	
	operator fun get(index: Long): Boolean
}