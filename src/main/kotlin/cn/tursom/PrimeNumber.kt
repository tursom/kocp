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
		val ret = ArrayList<Long>()
		var (a, b) = doubleDecomposition(num)
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
		val sqrtMaxNumber = sqrt(checkedNumber.toDouble()).toLong() shr 1
		bitSet.down(0)
		for (i in 1..sqrtMaxNumber) {
			if (bitSet[i]) {
				val doubleI = i * 2 + 1
				for (j in ((doubleI * doubleI) shr 1)..bitSet.size step doubleI) {
					bitSet.down(j)
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

