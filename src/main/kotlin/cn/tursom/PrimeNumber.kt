package cn.tursom

import java.lang.Math.sqrt


object PrimeNumber : Iterable<Long> {
	private val bitSet = BitSet(defalutState = true)
	
	init {
		reCalc()
	}
	
	operator fun get(num: Long) = when {
		num < 2L -> false
		num == 2L -> true
		num and 1L != 1L -> false
		else -> {
			check(num)
			bitSet[num shr 1]
		}
	}
	
	private fun check(num: Long) {
		if (num >= bitSet.size * 2 && bitSet.resize(num shr 1)) {
			reCalc()
		}
	}
	
	private fun reCalc() = synchronized(this) {
		val sqrtMaxNumber = sqrt((bitSet.size * 2 + 1).toDouble()).toLong() + 1
		bitSet.down(0)
		for (i in 3..sqrtMaxNumber step 2) {
			if (bitSet[(i shr 1)]) {
				for (j in (i * 3)..(bitSet.size * 2 + 1) step (i * 2)) {
					bitSet.down(j shr 1)
				}
			}
		}
	}
	
	override fun iterator() = PrimeNumberIterator(Int.MAX_VALUE.toLong())
	
	fun iterator(max: Long) = PrimeNumberIterator(max)
	fun iterator(max: Int) = PrimeNumberIterator(max.toLong())
	
	class PrimeNumberIterator(private val max: Long = Long.MAX_VALUE) : Iterator<Long> {
		private var index = 2L
		
		override fun hasNext(): Boolean {
			while (!PrimeNumber[index]) index++
			return index < max
		}
		
		override fun next(): Long {
			while (!PrimeNumber[index]) index++
			return index++
		}
	}
}

fun time(action: () -> Unit): Long {
	val t1 = System.currentTimeMillis()
	action()
	val t2 = System.currentTimeMillis()
	return t2 - t1
}

fun main() {
	println(9223372036854775807.javaClass)
	println(time { println(PrimeNumber[1]) })
	println(time { println(PrimeNumber[1999999999]) })
//	PrimeNumber.iterator(100000000).forEach {
//		print("$it ")
//	}
//	println()
}