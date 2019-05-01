package cn.tursom

/**
 * 非自动扩容的可设置默认值的BitSet
 */
class BitArray(
	maxIndex: Long = -1,
	@Suppress("MemberVisibilityCanBePrivate") val defaultState: Boolean = false
) {
	private var bitSet = IntArray(needSize(maxIndex).toInt())
	val size
		get() = (bitSet.size.toLong() shl 5) - 1
	
	init {
		val default = if (defaultState) -1 else 0
		for (i in 0 until bitSet.size) {
			bitSet[i] = default
		}
	}
	
	operator fun get(index: Long): Boolean {
		check(index)
		return bitSet[(index ushr 5).toInt()] and (1 shl (index and 31).toInt()) != 0
	}
	
	operator fun set(index: Long, state: Boolean) {
		if (state) {
			up(index)
		} else {
			down(index)
		}
	}
	
	fun up(index: Long) {
		check(index)
		val arrayIndex = (index ushr 5).toInt()
		bitSet[arrayIndex] = bitSet[arrayIndex] or (1 shl (index and 31).toInt())
	}
	
	fun down(index: Long) {
		check(index)
		val arrayIndex = (index ushr 5).toInt()
		bitSet[arrayIndex] = bitSet[arrayIndex] and (1 shl (index and 31).toInt()).inv()
	}
	
	private fun check(index: Long) {
		if (index < 0) throw IndexOutOfBoundsException("bitIndex < 0: $index")
		if (index > size) throw IndexOutOfBoundsException("bitIndex > maxSize: $index, $size")
	}
	
	fun resize(maxIndex: Long = 0): Boolean = synchronized(this) {
		if (maxIndex <= size) return false
		
		val newSet = IntArray(needSize(maxIndex).toInt())
		
		bitSet.copyInto(newSet)
		
		val default = if (defaultState) -1 else 0
		for (i in bitSet.size until newSet.size) {
			newSet[i] = default
		}
		
		bitSet = newSet
		return true
	}
	
	companion object {
		@JvmStatic
		fun needSize(maxIndex: Long) = ((maxIndex shr 5) + 1) and 0x1fffffffffffffff
	}
}