package kocp.interactive

import kocp.orbit.Orbit
import server.Interactive
import server.RandomCode

val exitCode = RandomCode()

val orbitMap = HashMap<String, Orbit>()
var orbit = Orbit()

val interactive = object : Interactive() {
	override val command: (command: String) -> Unit
		get() = {
			val commands = it.split("\\s+".toRegex())
			val iterator = commands.listIterator()
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
	exitCode.showCode("exit code")
	interactive.command("hey")
	println(HashMap<String, String>(mapOf(Pair("aa", "bb"),Pair("ab", "bb"))))
	//println(getCommandMap.toJson())
	//println(Gson().toJson(getCommandMap))
	//val field = Orbit::class.java.getField("perigee")
	//println(field.get(orbit))
	interactive.start()
}