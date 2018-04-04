package kocp

import kocp.orbit.cachedCenterBody
import kocp.orbit.CenterBody
import kocp.orbit.CenterBody.Companion.Earth
import kocp.orbit.Orbit

fun main(args: Array<String>) {
	val orbit = Orbit(
		10000.0, 10000.0, 0.0)
	cachedCenterBody(mass = 5.97237e24, radius = 6.371e6)
	CenterBody[Earth]
	println(CenterBody.centerBodyTable.count())
	println(orbit)
}

