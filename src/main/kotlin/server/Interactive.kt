package server

open class Interactive(
	private val indicator: String = ">>>",
	private val cantFindCommand: String = "can't understand command") : Thread() {
	private val input = System.`in`.bufferedReader()
	open val command: (command: String) -> Unit
		get() = { println("Hello $command!") }

	override fun run() {
		try {
			while (true) {
				print(indicator)
				val command = input.readLine()
				when (command) {
					"" -> {
					}
					else -> {
						try {
							this.command(command)
						} catch (e: CantFindCommandException) {
							println(cantFindCommand)
						}
					}
				}
			}
		} catch (e: Exception) {
			input.close()
			e.printStackTrace()
		}
		whenClose()
	}

	open fun whenClose() {}

	class CloseException : Exception()
	class CantFindCommandException : Exception()
}