package cn.tursom.server

import cn.tursom.PrimeNumber
import cn.tursom.datagram.server.SimplifyUdpServer

class PrimeNumberServer(
	@Suppress("MemberVisibilityCanBePrivate") val port: Int,
	threads: Int = 1
) {
	private val server = SimplifyUdpServer(port, threads) { data, size, address ->
		if (size < 8) {
			send(address, byteArrayOf(0))
			return@SimplifyUdpServer
		}
		
		val num = data.toLong()
		
		val decomposition = PrimeNumber.decomposition(num)
		val result = ByteArray(decomposition.size * 8 + 1)
		result[0] = if (PrimeNumber[num]) 1 else 0
		
		decomposition.forEachIndexed { index, l ->
			result.addLong(l, 1 + index * 8)
		}
		send(address, result)
	}
	
	fun start() {
		server.start()
	}
	
	fun stop() {
		server.close()
	}
}

fun ByteArray.addLong(num: Long, offset: Int = 0) {
	this[0 + offset] = (num shr (7 * 8)).toByte()
	this[1 + offset] = (num shr (6 * 8)).toByte()
	this[2 + offset] = (num shr (5 * 8)).toByte()
	this[3 + offset] = (num shr (4 * 8)).toByte()
	this[4 + offset] = (num shr (3 * 8)).toByte()
	this[5 + offset] = (num shr (2 * 8)).toByte()
	this[6 + offset] = (num shr (1 * 8)).toByte()
	this[7 + offset] = (num shr (0 * 8)).toByte()
}

fun Long.toByteArray(): ByteArray {
	val array = ByteArray(8)
	array[0] = shr(7 * 8).toByte()
	array[1] = shr(6 * 8).toByte()
	array[2] = shr(5 * 8).toByte()
	array[3] = shr(4 * 8).toByte()
	array[4] = shr(3 * 8).toByte()
	array[5] = shr(2 * 8).toByte()
	array[6] = shr(1 * 8).toByte()
	array[7] = shr(0 * 8).toByte()
	return array
}

fun ByteArray.toLong(): Long =
	(this[0].toLong() shl 56 and (0xff shl (8 * 7))) or
		(this[1].toLong() shl 48 and (0xff shl (8 * 6))) or
		(this[2].toLong() shl 40 and (0xff shl (8 * 5))) or
		(this[3].toLong() shl 32 and (0xff shl (8 * 4))) or
		(this[4].toLong() shl 24 and (0xff shl (8 * 3))) or
		(this[5].toLong() shl 16 and (0xff shl (8 * 2))) or
		(this[6].toLong() shl 8 and (0xff shl (8 * 1))) or
		(this[7].toLong() and 0xff)


fun ByteArray.toLong(offset: Int = 0): Long =
	(this[0 + offset].toLong() shl 56 and (0xff shl (8 * 7))) or
		(this[1 + offset].toLong() shl 48 and (0xff shl (8 * 6))) or
		(this[2 + offset].toLong() shl 40 and (0xff shl (8 * 5))) or
		(this[3 + offset].toLong() shl 32 and (0xff shl (8 * 4))) or
		(this[4 + offset].toLong() shl 24 and (0xff shl (8 * 3))) or
		(this[5 + offset].toLong() shl 16 and (0xff shl (8 * 2))) or
		(this[6 + offset].toLong() shl 8 and (0xff shl (8 * 1))) or
		(this[7 + offset].toLong() and 0xff)