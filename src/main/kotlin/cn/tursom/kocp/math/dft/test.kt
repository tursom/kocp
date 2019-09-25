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
		result[it - sequenceRange.first] = (s - 1) / result.size
	}
	return result
}

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

fun FloatArray.arrayMinus(from: FloatArray): FloatArray {
	val newArray = FloatArray(size)
	for (i in indices) {
		newArray[i] = this[i] - from[i]
		//println("$i: ${this[i]}-${from[i]}=${this[i] - from[i]}")
	}
	return newArray
}

fun polynomialSample(
	polynomial: Polynomial,
	from: Float = 0.0f,
	to: Float = PI.toFloat() * 2,
	numberOfSamples: Int = ((to - from) / PI).toInt() * 2 + 1
): FloatArray {
	val spacing = (to - from) / (numberOfSamples - 1)
	val ret = FloatArray(numberOfSamples)
	var position = from
	repeat(numberOfSamples) {
		ret[it] = polynomial[position]
		position += spacing
	}
	return ret
}

fun idft(ft: FloatArray, numberOfSamples: Int = (ft.size - 1) * 2): FloatArray {
	val result = FloatArray(numberOfSamples)
	ft.forEachIndexed { index, fl ->
		val srcSample = cosSample(0.0f, index * PI.toFloat() * 2, numberOfSamples)
		for (i in result.indices) {
			result[i] += srcSample[i] * fl
		}
	}
	return result
}

fun main() {
	val src =
		//polynomialSample(ArrayPolynomial(floatArrayOf(0.0f, 0.0f, 1.0f)), to = PI.toFloat() * 50, numberOfSamples = 200)
		cosSample(to = PI.toFloat() * 5000, numberOfSamples = 10000)
	//floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
	//	1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
	//println(src.asList())
	val dft = dft(src)
	//println(dft.asList())
	//dft(src).forEachIndexed { index, fl ->
	//	println("$index: $fl")
	//}
	val idft = idft(dft, 10000)
	//println(idft.asList())
	//println(src.arrayMinus(idft).asList())
	println(src.arrayMinus(idft).sum() / src.size)
}