package cn.tursom.kocp.interactive

import cn.tursom.kocp.orbit.Orbit
import cn.tursom.server.Interactive
import cn.tursom.server.RandomCode

val exitCode = RandomCode()

val orbitMap = HashMap<String /* orbit name */, Orbit /* orbit object */>()
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