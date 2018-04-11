package kocp.math

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlin.collections.HashSet

class HashSet<E> : HashSet<E>() {
	operator fun get(value: E): E {
		add(value)
		return elementAt(indexOf(value))
	}

	fun toJson() = Gson().toJson(this) ?: null

	companion object {
		inline fun <reified E> fromJson(json: String): HashSet<E>? {
			val hashSet = HashSet<E>()
			return try {
				Gson().fromJson(json, kocp.math.HashSet::class.java).forEach {
					hashSet.add(Gson().fromJson(it.toString(), E::class.java))
				}
				hashSet
			} catch (e: JsonSyntaxException) {
				null
			}
		}
	}
}