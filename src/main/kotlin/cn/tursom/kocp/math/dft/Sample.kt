package cn.tursom.kocp.math.dft

import kotlin.math.PI
import kotlin.math.cos

open class Sample(private val sample: FloatArray) {
	operator fun get(index: Int) = sample[index]
	fun dft(
		sequenceRange: IntRange = IntRange(0, sample.size / 2)
	): FloatArray {
		val result = FloatArray(sequenceRange.last - sequenceRange.first + 1)
		sequenceRange.forEach {
			val srcSample = cosSample(0.0f, it * PI.toFloat() * 2, sample.size)
			var s = 0.0f
			for (i in sample.indices) {
				s += sample[i] * srcSample[i]
			}
			result[it - sequenceRange.first] = (s - 1) / result.size
		}
		return result
	}

	companion object {
		@JvmStatic
		fun ift(ft: FloatArray, numberOfSamples: Int = (ft.size - 1) * 2): FloatArray {
			val result = FloatArray(numberOfSamples)
			ft.forEachIndexed { index, fl ->
				val srcSample = cosSample(0.0f, index * PI.toFloat() * 2, numberOfSamples)
				for (i in result.indices) {
					result[i] += srcSample[i] * fl
				}
			}
			return result
		}

		@JvmStatic
		fun cosSample(
			from: Float = 0.0f,
			to: Float = PI.toFloat() * 2,
			numberOfSamples: Int = ((to - from) / PI).toInt() + 1
		): FloatArray {
			val spacing = (to - from) / (numberOfSamples - 1)
			val ret = FloatArray(numberOfSamples)
			var position = from
			repeat(numberOfSamples) {
				ret[it] = cos(position)
				position += spacing
			}
			return ret
		}
	}
}