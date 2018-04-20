package cn.tursom.server

import java.lang.Thread.sleep
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
	val pool = ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, LinkedBlockingQueue(10))
	for (i in 1..100) {
		try {
			pool.execute {
				sleep(1000)
			}
		} catch (e: RejectedExecutionException) {
			System.err.println("$i:${e.toString()}")
			//e.printStackTrace()
		}
	}
}