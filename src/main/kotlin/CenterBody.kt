class CenterBody(GM: Double? = null, M: Double? = null) {
	val GM: Double = GM ?: (M ?: 0.0) * G
	
	companion object {
		const val G: Double = 6.67408e-11
	}
}