package server

import java.io.File
import java.util.*

class RandomCode(begin: Int = 10000000, end: Int = 99999999) {
	private val randomCode = "${Companion.randomInt(begin, end)}"

	override fun toString() = randomCode

	fun showCode(codeName: String = "passcode", filepath: String? = null) {
		println("$codeName: $randomCode")
		when (filepath) {
			null -> return
			else -> {
				val file = File(filepath)
				file.createNewFile()
				file.writeText("$codeName = $randomCode")
			}
		}
	}

	companion object {
		private fun randomInt(min: Int, max: Int) = Random().nextInt(max) % (max - min + 1) + min
	}
}