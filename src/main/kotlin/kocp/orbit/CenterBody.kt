package kocp.orbit

import kocp.math.Value

class CenterBody(GM: Double? = null, M: Double? = null, radius: Double = 0.0) : Value() {
	val GM: Double = GM ?: (M ?: 0.0) * G
	val radius: Double =
			if (radius >= 0) {
				radius
			} else {
				throw RadiusOutOfRangeException()
			}
	
	class RadiusOutOfRangeException(message: String? = null) : OutOfRangeException(message)
	
	companion object {
		const val G: Double = 6.67408e-11
	}
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as CenterBody
		
		if (GM != other.GM) return false
		if (radius != other.radius) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = GM.hashCode()
		result = 31 * result + radius.hashCode()
		return result
	}
	
	override fun toString(): String {
		return "CenterBody(GM=$GM, radius=$radius)"
	}
}