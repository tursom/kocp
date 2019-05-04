package cn.tursom.bitarray

import java.io.*

/**
 * 非自动扩容的可设置默认值的BitSet
 */
class MemoryBitArray(
	maxIndex: Long = -1,
	override val defaultState: Boolean = false
) : BitArray {
	private var bitSet = LongArray(needSize(maxIndex))
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
		return bitSet[(index shr 6).toInt()] and getArr[index.toInt() and 63] != 0L
	}

	override fun up(index: Long) {
//		check(index)
		val arrayIndex = (index shr 6).toInt()
		bitSet[arrayIndex] = bitSet[arrayIndex] or getArr[index.toInt() and 63]
	}

	override fun down(index: Long) {
//		check(index)
		val arrayIndex = (index shr 6).toInt()
		bitSet[arrayIndex] = bitSet[arrayIndex] and setArr[index.toInt() and 63]
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
		fun needSize(maxIndex: Long) = (((maxIndex shr 6) + 1) and 0xffffffff).toInt()

		private val getArr = longArrayOf(
			1L shl 0, 1L shl 1, 1L shl 2, 1L shl 3,
			1L shl 4, 1L shl 5, 1L shl 6, 1L shl 7,
			1L shl 8, 1L shl 9, 1L shl 10, 1L shl 11,
			1L shl 12, 1L shl 13, 1L shl 14, 1L shl 15,
			1L shl 16, 1L shl 17, 1L shl 18, 1L shl 19,
			1L shl 20, 1L shl 21, 1L shl 22, 1L shl 23,
			1L shl 24, 1L shl 25, 1L shl 26, 1L shl 27,
			1L shl 28, 1L shl 29, 1L shl 30, 1L shl 31,
			1L shl 32, 1L shl 33, 1L shl 34, 1L shl 35,
			1L shl 36, 1L shl 37, 1L shl 38, 1L shl 39,
			1L shl 40, 1L shl 41, 1L shl 42, 1L shl 43,
			1L shl 44, 1L shl 45, 1L shl 46, 1L shl 47,
			1L shl 48, 1L shl 49, 1L shl 50, 1L shl 51,
			1L shl 52, 1L shl 53, 1L shl 54, 1L shl 55,
			1L shl 56, 1L shl 57, 1L shl 58, 1L shl 59,
			1L shl 60, 1L shl 61, 1L shl 62, 1L shl 63
		)

		private val setArr = LongArray(64) { getArr[it].inv() }
	}
}