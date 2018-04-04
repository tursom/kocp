package kocp.math

open class Vector(open var x: Double = 0.0, open var y: Double = 0.0, open var z: Double = 0.0) {
	open fun length() = Math.sqrt(x * x + y * y + z * z)
	
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
	
	operator fun rem(value: Double) = Vector(
		x = x / value,
		y = y / value,
		z = z / value)
	
	operator fun plusAssign(vector: Vector) {
		x += vector.x
		y += vector.y
		z += vector.z
	}
	
	operator fun minusAssign(vector: Vector) {
		x -= vector.x
		y -= vector.y
		z -= vector.z
	}
	
	operator fun timesAssign(value: Double) {
		x *= value
		y *= value
		z *= value
	}
	
	operator fun divAssign(value: Double) {
		x /= value
		y /= value
		z /= value
	}
	
	operator fun remAssign(value: Double) {
		x /= value
		y /= value
		z /= value
	}
	
	override fun toString(): String {
		return "Vector(x=$x, y=$y, z=$z)"
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as Vector
		
		if (x != other.x) return false
		if (y != other.y) return false
		if (z != other.z) return false
		
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
