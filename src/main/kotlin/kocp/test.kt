package kocp

import kocp.orbit.CenterBody
import kocp.orbit.Orbit

fun main(args: Array<String>) {
	val orbit = Orbit(
		10000.0, 10000.0, 0.0)
	println(CenterBody.centerBodyTable.count())
	println(Orbit.fromJson(orbit.toJson()))
	println(CenterBody.centerBodyTable.count())
}

