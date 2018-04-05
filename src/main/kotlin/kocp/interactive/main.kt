package kocp.interactive

import kocp.math.HashMap
import kocp.orbit.Orbit
import server.Interactive
import kotlin.system.exitProcess

val orbitMap = HashMap<String, Orbit>()

val commandMap = object : HashMap<String, (Iterator<String>) -> Unit>() {
	init {
		this["hey"] = { println("hello") }
		this["exit"] = { exitProcess(0) }
	}
}

val interactive = object : Interactive() {
	override val command: (command: String) -> Unit
		get() = {
			val commands = it.split("\\s+".toRegex())
			val iterator = commands.iterator()
			iterator.forEach { item ->
				run {
					(commandMap[item] ?: {
						println("cannot find command $item")
					})(iterator)
				}
			}
		}
}

fun main(args: Array<String>) {
	interactive.command("hey there and")
	interactive.start()
}