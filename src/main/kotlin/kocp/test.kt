package kocp

import kocp.orbit.CenterBody
import kocp.orbit.Orbit
import kotlin.math.PI

fun main(args: Array<String>) {
	val orbit = Orbit(10000.0, 10000.0, 0.0, CenterBody(GM = 1000.0, radius = 10000.0))
	println(orbit.apogee)
	println(orbit.perigee)
	println(orbit)
	println(orbit.orbitOvalParameterS)
	println(orbit.getArea(0.0, PI * 2))
	println(PI * 10000.0 * 10000.0)
}