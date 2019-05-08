package cn.tursom.datagram.server

import java.net.*

class SimplifyUdpServer(
	override val port: Int,
	@Suppress("MemberVisibilityCanBePrivate") val threads: Int = Runtime.getRuntime().availableProcessors(),
	@Suppress("MemberVisibilityCanBePrivate") val packageSize: Int = UdpPackageSize.internetLen,
	val handler: SimplifyUdpServer.(data: ByteArray, size: Int, address: SocketAddress) -> Unit
) : UDPServer {
	
	private val socket = DatagramSocket(port)
	
	override fun start() {
		for (i in 1..threads) {
			Thread(this, "SUdpSer$i").start()
		}
	}
	
	override fun run() {
		val inBuff = ByteArray(packageSize)
		val inPacket = DatagramPacket(inBuff, inBuff.size)
		while (true) {
			try {
				socket.receive(inPacket)
				handler(inPacket.data, inPacket.length, inPacket.socketAddress)
			} catch (e: SocketException) {
				if (e.message == "Socket closed" || e.message == "socket closed") {
					break
				} else {
					e.printStackTrace()
				}
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}
	
	fun send(address: SocketAddress, buffer: ByteArray) {
		send(address, buffer, buffer.size)
	}
	
	fun send(address: SocketAddress, buffer: ByteArray, size: Int) {
		socket.send(DatagramPacket(buffer, size, address))
	}
	
	fun send(address: SocketAddress, string: String) {
		send(address, string.toByteArray())
	}
	
	override fun close() {
		socket.close()
	}
}