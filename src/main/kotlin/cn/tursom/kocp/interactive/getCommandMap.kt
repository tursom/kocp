package cn.tursom.kocp.interactive

import com.google.gson.Gson
import cn.tursom.kocp.math.HashMap
import cn.tursom.kocp.orbit.CenterBody
import cn.tursom.kocp.orbit.CenterBody.Companion.Earth
import cn.tursom.kocp.orbit.Orbit
import kotlin.reflect.full.memberProperties

val getCommandMap = object : HashMap<String, (ListIterator<String>) -> Unit>() {
	init {
		this["apogee"] = { print(orbit.apogee) }
		this["perigee"] = { print(orbit.perigee) }
		this["velocityOfPerigee"] = { print(orbit.velocityOfPerigee) }
		this["locationOfPerigee"] = { print(orbit.locationOfPerigee) }
		this["value"] = { item ->
			run {
				when (item.hasNext()) {
					true -> {
						var command: String? = null
						try {
							command = orbitValuesMap[item.next()]
							println(Orbit::class.memberProperties.stream().filter { it.name == command }.findAny().get().invoke(orbit))
						} catch (e: java.lang.NoSuchMethodException) {
							println("cant find such value: $command}")
						} catch (e: java.util.NoSuchElementException) {
							println("cant find such value $command")
						}
					}
					false -> {
						println("show me what you need")
					}
				}
			}
		}
		this["values"] = { println(orbit.toJson()) }
		this["orbit"] = {
			if (it.hasNext()) println(orbitMap[it.next()])
			else println(orbit.toJson())
		}
		this["orbits"] = { println(Gson().toJson(orbitMap.keys)) }
		this["orbitsJson"] = { println(Gson().toJson(orbitMap)) }
		this["centerBodys"] = { println(CenterBody.names) }
		this["centerBodysJson"] = { println(CenterBody.json) }
	}
}