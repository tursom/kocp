package kocp.interactive

import kocp.orbit.Orbit
import server.Interactive
import server.RandomCode

val exitCode = RandomCode()

val orbitMap = kocp.math.HashMap<String /* orbit name */, Orbit /* orbit object */>()
var orbit = Orbit()

val interactive = object : Interactive() {
	override fun command(command: String) {
		val commands = command.split("\\s+".toRegex())
		val iterator = commands.listIterator()
		iterator.forEach {
			(commandMap[it] ?: {
				println("cannot find command $it")
			})(iterator)
		}
	}
}

fun main(args: Array<String>) {
	exitCode.showCode("exit code")
	interactive.command("hey")
	println(HashMap<String, String>(mapOf(Pair("aa", "bb"), Pair("ab", "bb"))))
//	println(getCommandMap.toJson())
//	println(Gson().toJson(getCommandMap))
//	val field = Orbit::class.java.getField("perigee")
//	println(field.get(orbit))
	interactive.start()
}