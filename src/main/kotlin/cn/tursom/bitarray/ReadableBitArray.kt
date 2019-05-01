package cn.tursom.bitarray

interface ReadableBitArray {
	val size: Long
	
	operator fun get(index: Long): Boolean
}