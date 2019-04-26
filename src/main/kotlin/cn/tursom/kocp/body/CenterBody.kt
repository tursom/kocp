package cn.tursom.kocp.body

import cn.tursom.kocp.math.get
import cn.tursom.kocp.orbit.Orbit

class CenterBody private constructor(
	mass: Double,
	radius: Double,
	/**
	 * null if it is an system's central body
	 */
	override val orbit: Orbit? = null,
	override val name: String? = null
) : Body {
	
	override val mass: Double =
		if (mass >= 0.0) {
			mass
		} else {
			throw MassOutOfRangeException()
		}
	
	val radius: Double =
		if (radius >= 0.0) {
			radius
		} else {
			throw RadiusOutOfRangeException()
		}
	
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as CenterBody
		
		if (name != other.name) return false
		if (mass != other.mass) return false
		if (radius != other.radius) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + mass.hashCode()
		result = 31 * result + radius.hashCode()
		result = 31 * result + orbit.hashCode()
		return result
	}
	
	override fun toString() =
		if (name != null && name != "") "CenterBody(name=$name, mass=$mass , GM=$GM, radius=$radius)"
		else "CenterBody(mass=$mass, GM=$GM, radius=$radius)"
	
	companion object {
		private val centerBodyTable = HashSet<CenterBody>()
		val set: Set<CenterBody>
			get() = centerBodyTable
		
		val Earth = get(5.97237e24, radius = 6.371e6, name = "Earth")
		
		operator fun get(
			mass: Double,
			radius: Double,
			orbit: Orbit? = null,
			name: String? = null
		) = centerBodyTable[CenterBody(mass, radius, orbit, name)]
	}
}

