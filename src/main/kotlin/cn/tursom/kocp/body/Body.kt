package cn.tursom.kocp.body

import cn.tursom.kocp.math.Value
import cn.tursom.kocp.orbit.Orbit

interface Body : Value {
	val name: String?
	val mass: Double
	val orbit: Orbit?
	
	override fun toString(): String
}

class RadiusOutOfRangeException(message: String? = null) : Value.OutOfRangeException(message)
class MassOutOfRangeException(message: String? = null) : Value.OutOfRangeException(message)

const val G: Double = 6.67408e-11
val Body.GM
	get() = mass * G