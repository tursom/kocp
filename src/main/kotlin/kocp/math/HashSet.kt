package kocp.math

import com.google.gson.Gson
import kotlin.collections.HashSet

class HashSet<E> : HashSet<E>() {
	operator fun get(value: E): E {
		val index = indexOf(value)
		return if (index == -1) {
			add(value)
			value
		} else elementAt(index)
	}

	fun toJson() = Gson().toJson(this) ?: "{}"

	fun fromJson(json:String){
		Gson().fromJson(json,javaClass)
	}
}