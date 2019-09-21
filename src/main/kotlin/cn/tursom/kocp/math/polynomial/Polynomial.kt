package cn.tursom.kocp.math.polynomial

interface Polynomial {
	fun calc(x: Float): Float
	operator fun get(x: Float) = calc(x)

	fun getA(index: Int): Float
	operator fun get(index: Int) = getA(index)

	val size: Int
	val n get() = size
}

open class ArrayPolynomial private constructor(private val aArray: FloatArray) : Polynomial {
	override val size: Int get() = aArray.size

	constructor(aArray: FloatArray, from: Int = 0, to: Int = aArray.size) : this(aArray.copyOfRange(from, to))

	override fun calc(x: Float): Float {
		if (aArray.isEmpty()) return 0.0f
		var ret = aArray.last()
		for (i in aArray.size - 2 downTo 0) {
			ret = aArray[i] + x * ret
		}
		return ret
	}

	override fun getA(index: Int): Float = aArray[index]

	override fun toString(): String {
		if (aArray.isEmpty()) return ""
		val sb = StringBuilder()
		var sIndex = 0
		for (i in aArray.indices) {
			val a = aArray[i]
			if (a == 0.0f) continue
			sIndex = i
			break
		}
		when (sIndex) {
			0 -> sb.append(aArray[sIndex])
			1 -> if (aArray[sIndex] != 1.0f) sb.append("${aArray[sIndex]}*x") else sb.append("x")
			else -> if (aArray[sIndex] != 1.0f) sb.append("${aArray[sIndex]}*x^$sIndex") else sb.append("x^$sIndex")
		}

		for (i in sIndex + 1 until aArray.size) {
			val a = aArray[i]
			if (a == 0.0f) continue
			sb.append(" + ${a}*x^$i")
		}
		return sb.toString()
	}
}


fun main() {
	val polynomial = ArrayPolynomial(floatArrayOf(0.0f, 0.0f, 1.0f))
	println(polynomial)
	for (i in 0..20) {
		val index = i.toFloat() / 10
		println("$index: ${polynomial[index]}")
	}
}