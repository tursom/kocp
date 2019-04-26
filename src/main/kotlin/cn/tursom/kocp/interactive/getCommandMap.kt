package cn.tursom.kocp.interactive

import cn.tursom.kocp.body.CenterBody
import cn.tursom.kocp.math.toJson
import cn.tursom.kocp.orbit.OvalOrbit
import com.google.gson.Gson
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
							println(OvalOrbit::class.memberProperties.stream().filter { it.name == command }.findAny().get().invoke(orbit))
						} catch (e: NoSuchMethodException) {
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
		this["centerBodys"] = { println(CenterBody.set) }
		this["centerBodysJson"] = { println(CenterBody.set.toJson()) }
	}
}