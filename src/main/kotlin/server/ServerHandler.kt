package server

import java.net.Socket

/*
 * ServerHandler请求处理类
 * 通过重载handle()函数处理逻辑
 * recv()提供了网络通讯常见的recv函数，避免getLine造成的阻塞
 * 自动关闭套接字，自动处理异常（全局）
 * 通拥有较好的异常处理体系，可通过异常实现基本的逻辑
 * 可以处理异常的同时给客户端发送异常信息，通过重载ServerException.code的getter实现
 */
abstract class ServerHandler(val socket: Socket?) : Runnable {
	protected val inputStream = socket?.getInputStream()
	protected val outputStream = socket?.getOutputStream()
	
	init {
		if (socket?.isClosed != false) {
			System.err.println("socket closed")
			throw SocketClosedException()
		}
		if (inputStream == null) {
			System.err.println("input stream cannot use")
			socket.close()
			throw InputStreamCantUseException("input stream cannot use")
		}
		if (outputStream == null) {
			System.err.println("output stream cannot use")
			closeInputStream()
			socket.close()
			throw OutputStreamCantUseException("output stream cannot use")
		}
	}
	
	final override fun run() {
		try {
			handle()
		} catch (e: ServerException) {
			if (e.message == null)
				e.printStackTrace()
			else
				System.err.println("${e::class.java}:${e.message}")
			outputStream?.write(e.code ?: serverError ?: Companion.serverError)
		} catch (e: Exception) {
			e.printStackTrace()
			outputStream?.write(serverError ?: Companion.serverError)
		}
		closeSocket()
	}
	
	abstract fun handle()
	
	protected fun recv(maxsize: Int): String? {
		if (socket?.isClosed != false) return null
		val buffer = ByteArray(maxsize)
		val size: Int
		try {
			size = inputStream?.read(buffer, 0, maxsize) ?: return null
		} catch (e: StringIndexOutOfBoundsException) {
			System.err.println("connection ${socket.inetAddress} closed")
			return null
		}
		return String(buffer, 0, size)
	}
	
	private fun closeSocket() {
		if (socket?.isClosed != true) {
			closeInputStream()
			closeOutputStream()
			socket?.close()
		}
	}
	
	private fun closeInputStream() {
		try {
			inputStream?.close()
		} catch (e: Exception) {
		}
	}
	
	private fun closeOutputStream() {
		try {
			outputStream?.close()
		} catch (e: Exception) {
		}
	}
	
	open class ServerException(s: String? = null) : Exception(s) {
		open val code: ByteArray?
			get() = null
	}
	
	class SocketClosedException(s: String? = null) : ServerException(s)
	class InputStreamCantUseException(s: String? = null) : ServerException(s)
	class OutputStreamCantUseException(s: String? = null) : ServerException(s)
	
	open val serverError: ByteArray?
		get() = null
	
	companion object Companion {
		const val debug: Boolean = true
		val serverError = "server error".toByteArray()
	}
	
}

