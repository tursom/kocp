package kocp.math

import com.google.gson.Gson
import kotlin.collections.HashMap

class HashMap<K, V> : HashMap<K, V>() {
	fun toJson() = Gson().toJson(this) ?: "{}"

	fun fromJson(json: String) = Gson().fromJson(json, this::class.java)!!

	companion object {
		fun fromJson(json: String) = Gson().fromJson(json, kocp.math.HashMap::class.java)!!
	}
}