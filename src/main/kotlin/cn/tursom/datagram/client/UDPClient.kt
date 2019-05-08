package cn.tursom.datagram.client

import java.io.Closeable
import java.net.*


class UdpClient(
	private val host: String,
	private val port: Int,
	private val packageSize: Int = defaultLen
) : Closeable {
	
	private val socket = DatagramSocket()
	
	fun send(
		data: ByteArray,
		timeout: Int = 0,
		callback: ((ByteArray, size: Int) -> Unit)? = null
	) {
		val address = InetSocketAddress(host, port)
		socket.send(DatagramPacket(data, data.size, address))
		//定义接受网络数据的字节数组
		recv(address, ByteArray(packageSize), timeout, callback ?: return)
	}
	
	fun recv(address: SocketAddress, buffer: ByteArray, callback: (ByteArray, size: Int) -> Unit) {
		val inPacket = DatagramPacket(buffer, buffer.size, address)
		socket.receive(inPacket)
		callback(inPacket.data ?: return, inPacket.length)
	}
	
	fun recv(
		address: SocketAddress,
		buffer: ByteArray,
		timeout: Int,
		callback: (ByteArray, size: Int) -> Unit
	) {
		socket.soTimeout = timeout
		recv(address, buffer, callback)
	}
	
	override fun close() {
		socket.close()
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	companion object {
		//定义不同环境下数据报的最大大小
		const val LANNetLen = 1472
		const val internetLen = 548
		const val defaultLen = internetLen
	}
}