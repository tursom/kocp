package kocp

import kocp.orbit.Orbit
import kocp.math.HashMap

val orbitTable = HashMap<String, Orbit>()

fun main(args: Array<String>) {
	val orbit = Orbit(10000.0, 10000.0, 0.0)
	orbitTable["test1"] = orbit
	orbitTable["test2"] = orbit
	val orbit2 = Orbit(HashMap.fromJson(orbitTable.toJson())["test1"].toString())
	println(orbit2)
}

