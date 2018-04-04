package kocp.math

class UnitVector(x: Double, y: Double, z: Double) : Vector(x, y, z) {
	init {
		val length = length()
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
	override var y: Double
		get() = super.y
		set(value) {
			val length = Vector(x, value, z).length()
			super.x /= length
			super.y = value / length
			super.z /= length
		}
	override var z: Double
		get() = super.z
		set(value) {
			val length = Vector(x, y, value).length()
			super.x /= length
			super.y /= length
			super.z = value / length
		}
}