package cn.tursom

import cn.tursom.datagram.client.UdpClient
import cn.tursom.server.PrimeNumberServer
import cn.tursom.server.toByteArray
import cn.tursom.server.toLong
import org.junit.Test
import java.net.SocketTimeoutException

class PrimeNumberServerTest {
	fun UdpClient.getPrimeNumber(num: Long) {
		send(num.toByteArray(), 10_000) { bytes, size ->
			println("$num ${if (bytes[0] == 0.toByte()) "不" else ""}是质数")
			print("因数分解: ")
			for (j in 1 until size step 8) {
				print("${bytes.toLong(j)} ")
			}
			println()
			println()
		}
	}
	
	@Test
	fun test() {
		val port = 12345
		val server = PrimeNumberServer(port)
		server.start()
		
		UdpClient("127.0.0.1", port).use {
			for (i in 1..100L) {
				while (true) {
					try {
						it.getPrimeNumber(i)
						break
					} catch (e: SocketTimeoutException) {
					}
				}
			}
		}
		
		server.stop()
	}
}