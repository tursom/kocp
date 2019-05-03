package cn.tursom.bitarray

import java.io.*

/**
 * 非自动扩容的可设置默认值的BitSet
 */
class MemoryBitArray(
	maxIndex: Long = -1,
	override val defaultState: Boolean = false
) : BitArray {
	private var bitSet = LongArray(needSize(maxIndex).toInt())
	override val size
		get() = (bitSet.size.toLong() shl 6) - 1
	val usedSize
		get() = bitSet.size * 8
	val usedSizeStr: String
		get() {
			val memory = usedSize
			val b = memory % 1024
			val kb = (memory / 1024) % 1024
			val mb = (memory / 1024 / 1024) % 1024
			return "${if (mb != 0) "$mb MB " else ""}${if (kb != 0) "$kb KB " else ""} $b Byte"
		}
	
	init {
		val default = if (defaultState) -1L else 0L
		for (i in 0 until bitSet.size) {
			bitSet[i] = default
		}
	}
	
	override operator fun get(index: Long): Boolean {
//		check(index)
		return bitSet[(index shr 6).toInt()] and ((1L shl ((index and 63).toInt()))) != 0L
	}
	
	override fun up(index: Long) {
//		check(index)
		val arrayIndex = (index ushr 6).toInt()
		bitSet[arrayIndex] = bitSet[arrayIndex] or (1L shl (index and 63).toInt())
	}
	
	override fun down(index: Long) {
//		check(index)
		val arrayIndex = (index ushr 6).toInt()
		bitSet[arrayIndex] = bitSet[arrayIndex] and (1L shl (index and 63).toInt()).inv()
	}
	
	override fun upAll() {
		for (i in 0 until bitSet.size) {
			bitSet[i] = -1
		}
	}
	
	override fun downAll() {
		for (i in 0 until bitSet.size) {
			bitSet[i] = 0
		}
	}
	
	private fun check(index: Long) {
		if (index < 0) throw IndexOutOfBoundsException("bitIndex < 0: $index")
		if (index > size) throw IndexOutOfBoundsException("bitIndex > maxSize: $index, $size")
	}
	
	override fun resize(maxIndex: Long): Boolean = synchronized(this) {
		if (maxIndex <= size) return false
		
		val newSet = LongArray(needSize(maxIndex).toInt())
		
		bitSet.copyInto(newSet)
		
		val default = if (defaultState) -1L else 0
		for (i in bitSet.size until newSet.size) {
			newSet[i] = default
		}
		
		bitSet = newSet
		return true
	}
	
	override fun save(outputStream: OutputStream) {
		ObjectOutputStream(outputStream).use {
			it.writeObject(bitSet)
		}
	}
	
	override fun load(inputStream: InputStream) {
		ObjectInputStream(inputStream).use {
			bitSet = it.readObject() as LongArray
		}
	}
	
	fun copy(): MemoryBitArray {
		val newBitArray = MemoryBitArray(-1, defaultState)
		newBitArray.bitSet = bitSet.copyOf()
		return newBitArray
	}
	
	fun copy(size: Long): MemoryBitArray {
		val newBitArray = MemoryBitArray(-1, defaultState)
		newBitArray.bitSet = bitSet.copyOf((size shr 6).toInt())
		return newBitArray
	}
	
	fun copyOfRange(fromIndex: Long, toIndex: Long): MemoryBitArray {
		val newBitArray = MemoryBitArray(-1, defaultState)
		newBitArray.bitSet = bitSet.copyOfRange((fromIndex shr 6).toInt(), (toIndex shr 6).toInt())
		return newBitArray
	}
	
	companion object {
		@JvmStatic
		fun needSize(maxIndex: Long) = ((maxIndex shr 6) + 1) and 0x1fffffffffffffff
	}
}