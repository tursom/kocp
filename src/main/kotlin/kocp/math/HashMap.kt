package kocp.math

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlin.collections.HashMap

open class HashMap<K, V> : HashMap<K, V>() {
	fun toJson() = Gson().toJson(this) ?: "{}"

	companion object {
		inline fun <reified K, reified V : Any> fromJson(json: String): HashMap<K, V>? {
			val hashMap = HashMap<K, V>()
			return try {
				val gson = Gson().fromJson(json, kocp.math.HashMap::class.java)
				gson.forEach {
					hashMap[it.key as K] = (Gson().fromJson(it.value.toString(), V::class.java) ?: return null)
				}
				hashMap
			} catch (e: JsonSyntaxException) {
				null
			}
		}
	}
}