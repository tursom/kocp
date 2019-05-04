package cn.tursom.bitarray

import java.io.*

class FileBitArray(
	val fileName: String,
	override val defaultState: Boolean = false
) : BitArray {
	private val randomAccessFile = run {
		File(fileName).delete()
		RandomAccessFile(fileName, "rws")
	}
	
	private val cache = IntArray(256)
	private var cacheIndex = -1L
	private var cacheSize = (randomAccessFile.length() - 1) / 1024 + 1
	override val maxIndex
		get() = randomAccessFile.length() * 8 - 1
	
	override fun get(index: Long): Boolean {
		check(index)
		return (cache[arrayIndex(index)] and ((1 shl ((index and 31).toInt())))) != 0
	}
	
	override fun up(index: Long) {
		check(index)
		val arrayIndex = arrayIndex(index)
		cache[arrayIndex] = cache[arrayIndex] or (1 shl (index and 31).toInt())
	}
	
	override fun down(index: Long) {
		check(index)
		val arrayIndex = arrayIndex(index)
		cache[arrayIndex] = cache[arrayIndex] and (1 shl (index and 31).toInt()).inv()
	}
	
	override fun upAll() {
		val buffer = ByteArray(1024) { -1 }
		randomAccessFile.seek(0)
		for (i in 0 until cacheSize) {
			randomAccessFile.write(buffer)
		}
		for (i in 0 until cache.size) {
			cache[i] = -1
		}
	}
	
	override fun downAll() {
		val buffer = ByteArray(1024) { 0 }
		randomAccessFile.seek(0)
		for (i in 0 until cacheSize) {
			randomAccessFile.write(buffer)
		}
		for (i in 0 until cache.size) {
			cache[i] = 0
		}
	}
	
	override fun resize(maxIndex: Long): Boolean {
		if (maxIndex <= this.maxIndex) return false
		FileOutputStream(File(fileName), true).use {
			val byte = (if (defaultState) 0xff else 0x00).toByte()
			val byteArray = ByteArray(1024) { byte }
			val newCacheSize = (maxIndex - this.maxIndex - 1) / 1024 / 32 + 1
			for (i in 0 until newCacheSize) {
				it.write(byteArray)
				cacheSize++
			}
		}
		return true
	}
	
	override fun save(outputStream: OutputStream) {
		outputStream.use {
			val buffer = ByteArray(1024)
			randomAccessFile.seek(0)
			var readSize = randomAccessFile.read(buffer)
			while (readSize != 0) {
				it.write(buffer, 0, readSize)
				readSize = randomAccessFile.read(buffer)
			}
		}
	}
	
	override fun load(inputStream: InputStream) {
		inputStream.use {
			val buffer = ByteArray(1024)
			randomAccessFile.seek(0)
			var readSize = it.read(buffer)
			while (readSize != 0) {
				randomAccessFile.write(buffer, 0, readSize)
				readSize = it.read(buffer)
			}
		}
	}
	
	private fun check(index: Long) {
		if (index < 0) throw IndexOutOfBoundsException("bitIndex < 0: $index")
		if (index > maxIndex) throw IndexOutOfBoundsException("bitIndex > maxSize: $index, $maxIndex")
		val cacheIndex = (index shr 15)
		if (this.cacheIndex != cacheIndex) loadCache(cacheIndex)
	}
	
	private fun loadCache(cacheIndex: Long) {
		if (fileIndex(this.cacheIndex)) {
			cache.forEach(randomAccessFile::writeInt)
		}
		this.cacheIndex = cacheIndex
		fileIndex(this.cacheIndex)
		for (i in 0..255) {
			cache[i] = randomAccessFile.readInt()
		}
	}
	
	private fun fileIndex(index: Long): Boolean {
		if (index < 0) return false
		randomAccessFile.seek(index * 1024)
		return true
	}
	
	private fun arrayIndex(index: Long) = (index ushr 5).toInt() and 255
}