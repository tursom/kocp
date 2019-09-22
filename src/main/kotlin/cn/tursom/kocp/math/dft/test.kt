package cn.tursom.kocp.math.dft

import cn.tursom.kocp.math.polynomial.Polynomial
import kotlin.math.PI
import kotlin.math.cos


fun dft(
	sample: FloatArray,
	sequenceRange: IntRange = IntRange(0, sample.size / 2)
): FloatArray {
	val result = FloatArray(sequenceRange.last - sequenceRange.first + 1)
	sequenceRange.forEach {
		val srcSample = cosSample(0.0f, it * PI.toFloat() * 2, sample.size)
		var s = 0.0f
		for (i in sample.indices) {
			s += sample[i] * srcSample[i]
		}
		result[it - sequenceRange.first] = s - 1
	}
	return result
}

fun cosSample(from: Float = 0.0f, to: Float = PI.toFloat() * 2, numberOfSamples: Int = 10): FloatArray {
	val spacing = (to - from) / (numberOfSamples - 1)
	val ret = FloatArray(numberOfSamples)
	var position = from
	repeat(numberOfSamples) {
		ret[it] = cos(position)
		position += spacing
	}
	return ret
}

fun floatArrayPlus(a: FloatArray, b: FloatArray): FloatArray {
	for (i in a.indices) {
		a[i] += b[i]
	}
	return a
}

fun FloatArray.arrayPlus(from: FloatArray): FloatArray {
	for (i in indices) {
		this[i] += from[i]
	}
	return this
}


fun polynomialSample(polynomial: Polynomial, from: Float = 0.0f, to: Float = PI.toFloat() * 2, numberOfSamples: Int = 10): FloatArray {
	val spacing = (to - from) / (numberOfSamples - 1)
	val ret = FloatArray(numberOfSamples)
	var position = from
	repeat(numberOfSamples) {
		ret[it] = polynomial[position]
		position += spacing
	}
	return ret
}


fun main() {
	val src =
	//polynomialSample(ArrayPolynomial(floatArrayOf(0.0f, 0.0f, 1.0f)), to = PI.toFloat() * 50, numberOfSamples = 200)
		//cosSample(to = PI.toFloat() * 20, numberOfSamples = 50)
		floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
	println(dft(src).asList())
	println(src.asList())
}