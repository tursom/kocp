package kocp.orbit

fun cachedCenterBody(GM: Double? = null, mass: Double? = null, radius: Double = 0.0, name: String? = null): CenterBody {
	return CenterBody[CenterBody(GM = GM, mass = mass, radius = radius, name = name)]
}