package kocp.interactive

import com.google.gson.Gson
import kocp.math.HashMap
import kocp.orbit.Orbit
import java.io.File
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
		this["save"] = {
			if (it.hasNext()) {
				when (it.next()) {
					"orbit" -> {
						if (it.hasNext()) {
							orbitMap[it.next()] = orbit
						}
					}
					"file" -> {
						if (it.hasNext()) {
							val file = File(it.next())
							println(file.name)
							file.createNewFile()
							file.writeText(OrbitMember(orbit, orbitMap).toJson())
						}
					}
				}
			}
		}
	}
}

class OrbitMember(val orbit: Orbit, val orbitTable: HashMap<String, Orbit>) {
	fun toJson() = Gson().toJson(this) ?: "{}"
}

val setCommandMap = object : HashMap<String, (ListIterator<String>) -> Unit>(){
	init {

	}
}
