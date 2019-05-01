package cn.tursom.bitarray

import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface BitArray : ReadableBitArray {
	val defaultState: Boolean
	
	fun up(index: Long)
	fun upAll()
	
	fun down(index: Long)
	fun downAll()
	
	fun resize(maxIndex: Long = 0): Boolean
	
	fun save(outputStream: OutputStream)
	fun load(inputStream: InputStream)
}

operator fun BitArray.set(index: Long, state: Boolean) {
	if (state) {
		up(index)
	} else {
		down(index)
	}
}

fun BitArray.save(file: String) {
	save(File(file).outputStream())
}

fun BitArray.load(file: String) {
	load(File(file).inputStream())
}