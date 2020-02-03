package cn.tursom

import cn.tursom.core.usingTime
import com.google.gson.Gson
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock
import kotlin.random.Random

private val continuousDrawProbability = IntArray(101).also {
	var base = 2
	repeat(it.size) { i ->
		if (i > 50) {
			base += 2
		}
		it[i] = base
	}
	it[0] = 0
}

private fun IntArray.addAll(): Int {
	var result = 0
	forEach { result += it }
	return result
}

fun main() {
	val outTime = 1
	val checkingPerThread = 10000000
	val sampleSize = 500
	val threads = Runtime.getRuntime().availableProcessors()

	val result = IntArray(sampleSize)
	val processing = CountDownLatch(threads)
	val lock = ReentrantLock()

	val globalRandom = Random(System.currentTimeMillis())

	val usingTime = usingTime {
		repeat(threads) threads@{
			thread {
				val localRandom = Random(globalRandom.nextInt())
				val localResult = IntArray(sampleSize)
				repeat(checkingPerThread) checkingLoop@{
					var c = 0
					var n = 0
					localResult.indices.forEach { j ->
						if (localRandom.nextInt(0, 100) < continuousDrawProbability[c]) {
							c = 0
							if (localRandom.nextInt(0, 100) < 25) {
								n++
								if (n == outTime) {
									localResult[j] += 1
									return@checkingLoop
								}
							}
						}
						c++
					}
				}
				lock.withLock {
					localResult.forEachIndexed { index, l -> result[index] += l }
				}
				processing.countDown()
			}
		}

		processing.await()
	}
	println(result.asList())
	println(Gson().toJson(result))
	println("using $usingTime ms, checking ${result.addAll()}/${checkingPerThread * threads} samples")
}