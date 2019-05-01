package cn.tursom.bitarray

import java.io.RandomAccessFile

class FileReaderBitArray(
	fileName: String
) : ReadableBitArray {
	private val randomAccessFile = RandomAccessFile(fileName, "r")
	
	override val size: Long
		get() = randomAccessFile.length() * 8 - 1
	
	override fun get(index: Long): Boolean {
		randomAccessFile.seek(index shr 3)
		return (randomAccessFile.read() and ((1 shl ((index and 7).toInt())))) != 0
	}
}