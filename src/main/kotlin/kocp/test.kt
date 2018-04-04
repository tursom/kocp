package kocp

import kocp.orbit.CenterBody
import kocp.orbit.Orbit

fun main(args: Array<String>) {
	val orbit = Orbit(10000.0, 10000.0, 0.0, CenterBody(GM = 1000.0, radius = 10000.0))
	orbit.perigee = orbit.apogee * 10
	println(orbit)
}