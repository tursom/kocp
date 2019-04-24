package cn.tursom.kocp.math

import kotlin.math.sqrt

class UnitVector private constructor(
	x: Double,
	y: Double,
	z: Double,
	length: Double
) : Vector(
	x / length,
	y / length,
	z / length
) {
	constructor(x: Double, y: Double, z: Double) :
		this(x, y, z, sqrt(x * x + y * y + z * z))
	
	override val length = 1.0
	override val lengthSquare = 1.0
	
	override fun toString() = "UnitVector(x=$x, y=$y, z=$z)"
	
	companion object {
		val X
			get() = UnitVector(1.0, 0.0, 0.0)
		val Y
			get() = UnitVector(0.0, 1.0, 0.0)
		val Z
			get() = UnitVector(0.0, 0.0, 1.0)
	}
}