package cn.tursom.kocp.math

import com.google.gson.Gson

operator fun <E> HashSet<E>.get(value: E): E {
	val index = indexOf(value)
	return elementAt(if (index == -1) {
		synchronized(this) {
			add(value)
		}
		indexOf(value)
	} else {
		index
	})
}

fun Set<*>.toJson(): String = Gson().toJson(this)

fun Gson.fromJsonSet(json: String): HashSet<*>? = fromJson(json, HashSet::class.java)
