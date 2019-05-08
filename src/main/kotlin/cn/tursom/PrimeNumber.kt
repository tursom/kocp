package cn.tursom

import cn.tursom.bitarray.MemoryBitArray
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.sqrt

object PrimeNumber : Iterable<Long> {
	private val bitSet = MemoryBitArray(defaultState = true)
	val biggestNumber = sqrt(Long.MAX_VALUE.toDouble()).toLong()
	val checkedNumber
		get() = bitSet.maxIndex * 2 + 1
	
	operator fun get(num: Long) = when {
		num < 2L -> false
		num == 2L -> true
		num and 1L != 1L -> false
		num <= biggestNumber -> if (num > checkedNumber && num > biggestNumber shr 4) {
			checkBigNumber(num)
		} else {
			check(num)
			bitSet[num shr 1]
		}
		else -> checkBigNumber(num)
	}
	
	operator fun get(num: Int) = get(num.toLong())
	
	fun checkBigNumber(num: Long): Boolean {
		check(sqrt(num.toDouble()).toLong())
		untilIterator(sqrt(num.toDouble()).toLong()).forEach {
			if (num % it == 0L) return false
		}
		return true
	}
	
	fun save(outputStream: OutputStream) {
		bitSet.save(outputStream)
	}
	
	fun load(inputStream: InputStream) {
		bitSet.load(inputStream)
	}
	
	fun calc(num: Long) {
		check(num)
	}
	
	fun forEach(action: (num: Long) -> Unit) {
		action(2)
		for (i in 3..checkedNumber step 2) {
			if (get(i)) action(i)
		}
	}
	
	fun getUntil(max: Long, action: (num: Long) -> Unit) {
		if (max < 2) return
		action(2)
		for (i in 3..max step 2) {
			if (get(i)) action(i)
		}
	}
	
	fun getUntilEx(max: Long, action: (num: Long) -> Boolean) {
		if (max < 2) return
		if (!action(2)) return
		for (i in 3..max) {
			if (get(i) && !action(i)) return
		}
	}
	
	private fun doubleDecomposition(num: Long): Pair<Long, Long> {
		var ret = Pair(1L, num)
		getUntilEx(sqrt(num.toDouble()).toLong()) {
			if (num % it == 0L) {
				ret = Pair(it, num / it)
				return@getUntilEx false
			}
			return@getUntilEx true
		}
		return ret
	}
	
	fun decomposition(num: Long): LongArray {
		if (num == 0L) return longArrayOf(0L)
		
		val ret = ArrayList<Long>()
		var (a, b) = doubleDecomposition(if (num > 0) num else {
			ret.add(-1)
			-num
		})
		while (a != 1L) {
			ret.add(a)
			val (a2, b2) = doubleDecomposition(b)
			a = a2
			b = b2
		}
		ret.add(b)
		return ret.toLongArray()
	}
	
	override fun toString(): String {
//		return "PrimeNumber(checked number=${bitSet.maxIndex * 2}, used memory=${bitSet.usedSizeStr})"
		return "PrimeNumber(checked number=$checkedNumber, prime count=${bitSet.trueCount}, used memory=${bitSet.usedSizeStr})"
	}
	
	private fun check(num: Long) {
		if (
			num > (bitSet.maxIndex * 2) &&
			bitSet.resize(min(max(num shr 1, bitSet.maxIndex * 2)))
		) {
			reCalc()
		}
	}
	
	private fun max(a: Long, b: Long) = if (a > b) a else b
	
	private fun min(a: Long) = if (a > this.biggestNumber) this.biggestNumber else a
	
	private fun reCalc() {
		val sqrtMaxNumber = sqrt(checkedNumber.toDouble()).toLong() shr 1
		val bitSeiSize = bitSet.maxIndex
		bitSet.down(0)
		for (i in 1..sqrtMaxNumber) {
			if (bitSet[i]) {
				val doubleI = (i shl 1) + 1
				var j: Long = (doubleI * doubleI) shr 1
				while (j <= bitSeiSize) {
					bitSet.down(j)
					j += doubleI
				}
			}
		}
	}
	
	override fun iterator() = PrimeNumberIterator()
	
	class PrimeNumberIterator : Iterator<Long> {
		private var index = 2L
		
		override fun hasNext(): Boolean {
			while (index < checkedNumber && !bitSet[index shr 1]) index++
			return index < checkedNumber
		}
		
		override fun next(): Long {
			while (!bitSet[index shr 1]) index++
			return index++
		}
	}
	
	fun untilIterator(max: Long) = PrimeNumberUntilIterator(max)
	
	class PrimeNumberUntilIterator(val max: Long) : Iterator<Long> {
		private var index = 2L
		
		override fun hasNext(): Boolean {
			while (index < checkedNumber && index <= max && !bitSet[index shr 1]) index++
			return index < checkedNumber && index <= max
		}
		
		override fun next(): Long {
			while (!bitSet[index shr 1]) index++
			return index++
		}
	}
}

