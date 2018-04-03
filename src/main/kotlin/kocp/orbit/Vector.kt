package kocp.orbit

data class Vector(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
	fun length() = Math.sqrt(x * x + y * y + z * z)
	
	operator fun plus(vector: Vector): Vector {
		return Vector(
			x = x + vector.x,
			y = y + vector.y,
			z = z + vector.z)
	}
	
	operator fun minus(vector: Vector): Vector {
		return Vector(
			x = x - vector.x,
			y = y - vector.y,
			z = z - vector.z)
	}
	
	operator fun times(vector: Vector): Double {
		return x * vector.x + y * vector.y + z * vector.z
	}
	
	operator fun rem(vector: Vector): Vector {
		return Vector(
			x = y * vector.z - z * vector.y,
			y = z * vector.x - x * vector.z,
			z = x * vector.y - y * vector.x)
	}
	
	operator fun times(value: Double): Vector {
		return Vector(
			x = x * value,
			y = y * value,
			z = z * value)
	}
	
	operator fun rangeTo(vector: Vector): Double {
		return Math.acos((this * vector) / (this.length() * vector.length()))
	}
	
	operator fun rem(value: Double): Vector {
		return Vector(
			x = x / value,
			y = y / value,
			z = z / value)
	}
	
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
	
	operator fun remAssign(value: Double) {
		x /= value
		y /= value
		z /= value
	}
	
	companion object {
		val X = Vector(1.0, 0.0, 0.0)
		val Y = Vector(0.0, 1.0, 0.0)
		val Z = Vector(0.0, 0.0, 1.0)
	}
}
