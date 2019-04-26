package cn.tursom.kocp.math

open class Vector(
	open val x: Double = 0.0,
	open val y: Double = 0.0,
	open val z: Double = 0.0
) : Value {
	open val length
		get() = Math.sqrt(lengthSquare)
	
	open val lengthSquare
		get() = x * x + y * y + z * z
	
	val unit: UnitVector
		get() {
			val length = length
			return UnitVector(x = x / length, y = y / length, z = z / length)
		}
	
	operator fun unaryMinus() = Vector(-x, -y, -z)
	
	operator fun unaryPlus() = this
	
	infix operator fun plus(vector: Vector) = Vector(
		x = x + vector.x,
		y = y + vector.y,
		z = z + vector.z
	)
	
	infix operator fun plus(vector: Double) = Vector(
		x = x + vector,
		y = y + vector,
		z = z + vector
	)
	
	infix operator fun minus(vector: Vector) = Vector(
		x = x - vector.x,
		y = y - vector.y,
		z = z - vector.z
	)
	
	infix operator fun minus(vector: Double) = Vector(
		x = x - vector,
		y = y - vector,
		z = z - vector
	)
	
	infix operator fun times(vector: Vector) = x * vector.x + y * vector.y + z * vector.z
	
	infix operator fun times(value: Double) = Vector(
		x = x * value,
		y = y * value,
		z = z * value)
	
	infix operator fun rem(vector: Vector) = Vector(
		x = y * vector.z - z * vector.y,
		y = z * vector.x - x * vector.z,
		z = x * vector.y - y * vector.x
	)
	
	infix operator fun rem(vector: Double) = Vector(
		x = x % vector,
		y = y % vector,
		z = z % vector
	)
	
	infix operator fun div(value: Double) = Vector(
		x = x / value,
		y = y / value,
		z = z / value
	)
	
	infix operator fun rangeTo(vector: Vector) =
		Math.acos((this * vector) / (this.length * vector.length))
	
	override fun toString() = "Vector(x=$x, y=$y, z=$z)"
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		
		val value = try {
			other as Vector
		} catch (e: Exception) {
			return false
		}
		
		if (x != value.x) return false
		if (y != value.y) return false
		if (z != value.z) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = x.hashCode()
		result = 31 * result + y.hashCode()
		result = 31 * result + z.hashCode()
		return result
	}
	
}
