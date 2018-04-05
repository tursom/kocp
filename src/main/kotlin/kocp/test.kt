package kocp

import kocp.orbit.Orbit
import kocp.math.HashMap
import kocp.math.HashSet

val orbitTable = HashMap<String, Orbit>()
val orbitSet = HashSet<Orbit>()

fun main(args: Array<String>) {
	val orbit = Orbit(10000.0, 10000.0, 0.0)
	orbitSet.add(orbit)
	orbitSet.add(Orbit(10000.0, 10000.0, 0.0))
	println(orbitSet.count())
}

