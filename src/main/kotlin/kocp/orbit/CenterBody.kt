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
}