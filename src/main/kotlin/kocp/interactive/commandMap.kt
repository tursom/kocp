package kocp.interactive

import kocp.math.HashMap
import kocp.orbit.Orbit
import kotlin.reflect.full.memberProperties
import kotlin.system.exitProcess

val commandMap = object : HashMap<String, (ListIterator<String>) -> Unit>() {
	init {
		this["hey"] = { println("Welcome to use Tursom K. Ulefit's Kotlin Orbit Calc System") }
		this["exit"] = {
			when (it.hasNext()) {
				true -> when (it.next()) {
					exitCode.toString() -> exitProcess(0)
					else -> println("pass code wrong")
				}
				false -> {
					println("show me pass code")
				}
			}
		}
		this["show"] = {
			when (it.hasNext()) {
				true -> when (it.next()) {
					"list" -> {
						println("state:$orbit")
						println(orbitMap)
					}
				}
				false -> {
					println(orbit)
				}
			}
		}
		this["get"] = {
			(getCommandMap[it.next()] ?: {
				println("cannot find command ${kotlin.run {
					it.previous()
					it.next()
				}}")
			})(it)
		}
	}
}

val orbitValuesMap = object : HashMap<String, String>() {
	init {
		Orbit::class.memberProperties.forEach {
			this[it.name] = it.name
		}

	}
}

val getCommandMap = object : HashMap<String, (ListIterator<String>) -> Unit>() {
	init {
		this["apogee"] = { print(orbit.apogee) }
		this["perigee"] = { print(orbit.perigee) }
		this["velocityOfPerigee"] = { print(orbit.velocityOfPerigee) }
		this["locationOfPe"] = { print(orbit.locationOfPe) }
		this["value"] = { item ->
			run {
				when (item.hasNext()) {
					true -> {
						try {
							println(Orbit::class.memberProperties.stream().filter { it.name == orbitValuesMap[item.next()] }.findAny().get().invoke(orbit))
							//println(Orbit::class.java.getMethod("get${run {
							//val cs = item.next().toCharArray()
							//cs[0] = cs[0] - 32
							//String(cs)
							//}}").invoke(orbit))
						} catch (e: java.lang.NoSuchMethodException) {
							println("cant find such value")
						} catch (e: java.util.NoSuchElementException) {
							println("cant find such value")
						}
					}
					false -> {
						println("show me what you need")
					}
				}
			}
		}
		this["values"] = { println(orbit) }
	}
}
