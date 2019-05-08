package cn.tursom

import cn.tursom.datagram.client.UdpClient
import cn.tursom.server.PrimeNumberServer
import cn.tursom.server.toByteArray
import cn.tursom.server.toLong
import org.junit.Test
import java.net.SocketTimeoutException

class PrimeNumberServerTest {
	
	@Test
	fun test() {
		val port = 12345
		val server = PrimeNumberServer(port)
		server.start()
		
		UdpClient("127.0.0.1", port).use {
			for (i in -10..10L) {
				while (true) {
					try {
						it.send(i.toByteArray(), 10_000) { bytes, size ->
							println("$i ${if (bytes[0] == 0.toByte()) "不" else ""}是质数")
							val sb = StringBuilder()
							sb.append("$i =")
							for (j in 1 until size step 8) {
								sb.append(" ${bytes.toLong(j)} *")
							}
							sb.deleteCharAt(sb.length - 1)
							println(sb)
							println()
						}
						break
					} catch (e: SocketTimeoutException) {
					}
				}
			}
		}
		
		server.stop()
	}
}