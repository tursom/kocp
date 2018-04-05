package server

import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/*
 * SocketServer多线程服务器
 * 每当有新连接接入时就会将handler:Runnable加入线程池的任务队列中运行
 * 通过重载handler:Runnable的getter实现多态
 * start()函数实现无限循环监听，同时自动处理异常
 * 最新接入的套接字出存在socket变量中
 * 通过调用close()或closeServer()关闭服务器，造成的异常会被自动处理
 * cpuNumber是CPU处理器的个数
 */
open class SocketServer(
		port: Int, threads: Int = 1,
		queueSize: Int = 2147483647,
		timeout: Long = 0L,
		timeUnit: TimeUnit = TimeUnit.MILLISECONDS) : Thread() {
	
	var socket: Socket? = null
	private val pool = ThreadPoolExecutor(threads, threads, timeout, timeUnit, LinkedBlockingQueue(queueSize))
	private val serverSocket: ServerSocket = ServerSocket(port)
	
	override fun run() {
		while (!serverSocket.isClosed) {
			try {
				socket = serverSocket.accept()
				pool.execute(handler)
			} catch (e: IOException) {
				if (pool.isShutdown || serverSocket.isClosed) {
					System.err.println("server closed")
					break
				}
				e.printStackTrace()
			} catch (e: SocketException) {
				break
			} catch (e: RejectedExecutionException) {
				socket?.getOutputStream()?.write(poolIsFull)
			} catch (e: Exception) {
				e.printStackTrace()
				break
			}
		}
		whenClose()
		close()
		System.err.println("server closed")
	}
	
	open val handler: Runnable
		get() = Runnable {
			socket?.close()
		}
	
	fun closeServer() {
		if (!serverSocket.isClosed)
			serverSocket.close()
	}
	
	fun shutdownPool() {
		if (!pool.isShutdown)
			pool.shutdown()
	}
	
	open fun close() {
		shutdownPool()
		closeServer()
	}
	
	open fun whenClose() {
	}
	
	open val poolIsFull
		get() = "server pool is full".toByteArray()
	
	companion object {
		val cpuNumber = Runtime.getRuntime().availableProcessors()
	}
}