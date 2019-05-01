package cn.tursom

import cn.tursom.bitarray.MemoryBitArray
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.sqrt

object PrimeNumber : Iterable<Long> {
	private val bitSet = MemoryBitArray(defaultState = true)
	val biggestNumber = sqrt(Long.MAX_VALUE.toDouble()).toLong()
	val checkedNumber
		get() = bitSet.size * 2 + 1
	
	operator fun get(num: Long) = when {
		num < 2L -> false
		num == 2L -> true
		num and 1L != 1L -> false
		num <= biggestNumber -> {
			check(num)
			bitSet[num shr 1]
		}
		else -> run {
			check(biggestNumber)
			iterator(sqrt(num.toDouble()).toInt()).forEach {
				if (num % it == 0L) return@run false
			}
			true
		}
	}
	
	operator fun get(num: Int) = get(num.toLong())
	
	fun save(outputStream: OutputStream) {
		bitSet.save(outputStream)
	}
	
	fun load(inputStream: InputStream) {
		bitSet.load(inputStream)
	}
	
	override fun toString(): String {
//		return "PrimeNumber(checked number=${bitSet.size * 2}, used memory=${bitSet.usedSizeStr})"
		return "PrimeNumber(checked number=$checkedNumber)"
	}
	
	private fun check(num: Long) {
		if (
			num > (bitSet.size * 2) &&
			bitSet.resize(min(max(num shr 1, (bitSet.size * 1.5).toLong())))
		) {
			reCalc()
		}
	}
	
	private fun max(a: Long, b: Long) = if (a > b) a else b
	
	private fun min(a: Long) = if (a > this.biggestNumber) this.biggestNumber else a
	
	private fun reCalc() = synchronized(this) {
		val sqrtMaxNumber = sqrt(checkedNumber.toDouble()).toLong() + 1
		bitSet.down(0)
		for (i in 3..sqrtMaxNumber step 2) {
			if (bitSet[i shr 1]) {
				for (j in (i * 3)..checkedNumber step (i * 2)) {
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

