package kocp.interactive

import kocp.math.HashMap
import kocp.orbit.Orbit
import kotlin.reflect.full.memberProperties

val orbitValuesMap = object : HashMap<String, String>() {
	init {
		Orbit::class.memberProperties.forEach {
			this[it.name] = it.name
		}
		this["ap"] = "apogee"
		this["pe"] = "perigee"
		this["vpe"] = "velocityOfPerigee"
		this["lpe"] = "locationOfPerigee"
	}
}