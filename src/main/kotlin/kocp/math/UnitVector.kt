package kocp.math

class UnitVector(x: Double, y: Double, z: Double) : Vector(x, y, z) {
	init {
		val length = super.length()
		super.x /= length
		super.y /= length
		super.z /= length
	}
	
	override fun length() = 1.0
	
	override var x: Double
		get() = super.x
		set(value) {
			val length = Vector(value, y, z).length()
			super.x = value / length
			super.y /= length
			super.z /= length
		}
	
	override fun plusAssign(vector: Vector) {
		super.plusAssign(vector)
		toUnit()
	}
	
	override fun minusAssign(vector: Vector) {
		super.minusAssign(vector)
		toUnit()
	}
	
	override fun timesAssign(value: Double) {}
	
	override fun divAssign(value: Double) {}
	
	override var y: Double
		get() = super.y
		set(value) {
			super.y = value
			toUnit()
		}
	override var z: Double
		get() = super.z
		set(value) {
			super.z = value
			toUnit()
		}
	
	private fun toUnit() {
		val length = super.length()
		super.x /= length
		super.y /= length
		super.z /= length
	}
	
	override fun toString(): String {
		return "Unit${super.toString()}"
	}
}