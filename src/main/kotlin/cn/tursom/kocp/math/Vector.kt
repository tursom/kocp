package cn.tursom.kocp.math

open class Vector(
	open var x: Double = 0.0,
	open var y: Double = 0.0,
	open var z: Double = 0.0) {

	open fun length() = Math.sqrt(x * x + y * y + z * z)

	open fun lengthSquare() = x * x + y * y + z * z

	val unit: UnitVector
		get() {
			val length = length()
			return UnitVector(x / length, y / length, z / length)
		}

	fun negative() {
		x = -x
		y = -y
		z = -z
	}

	operator fun unaryMinus(): Vector {
		return Vector(-x, -y, -z)
	}

	operator fun plus(vector: Vector) = Vector(
		x = x + vector.x,
		y = y + vector.y,
		z = z + vector.z)

	operator fun minus(vector: Vector) = Vector(
		x = x - vector.x,
		y = y - vector.y,
		z = z - vector.z)

	operator fun times(vector: Vector) = x * vector.x + y * vector.y + z * vector.z

	operator fun rem(vector: Vector) = Vector(
		x = y * vector.z - z * vector.y,
		y = z * vector.x - x * vector.z,
		z = x * vector.y - y * vector.x)

	operator fun times(value: Double) = Vector(
		x = x * value,
		y = y * value,
		z = z * value)

	operator fun div(value: Double) = Vector(
		x = x / value,
		y = y / value,
		z = z / value)

	operator fun rangeTo(vector: Vector) = Math.acos((this * vector) / (this.length() * vector.length()))

	open operator fun plusAssign(vector: Vector) {
		x += vector.x
		y += vector.y
		z += vector.z
	}

	open operator fun minusAssign(vector: Vector) {
		x -= vector.x
		y -= vector.y
		z -= vector.z
	}

	open operator fun timesAssign(value: Double) {
		x *= value
		y *= value
		z *= value
	}

	open operator fun divAssign(value: Double) {
		x /= value
		y /= value
		z /= value
	}

	override fun toString(): String {
		return "Vector(x=$x, y=$y, z=$z)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true

		val value =
			try {
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

	companion object {
		val X
			get() = UnitVector(1.0, 0.0, 0.0)
		val Y
			get() = UnitVector(0.0, 1.0, 0.0)
		val Z
			get() = UnitVector(0.0, 0.0, 1.0)
	}
}
