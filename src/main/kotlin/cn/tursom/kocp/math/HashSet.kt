package cn.tursom.kocp.math

import com.google.gson.Gson

operator fun <E> HashSet<E>.get(value: E): E {
	add(value)
	return elementAt(indexOf(value))
}

fun HashSet<*>.toJson(): String = Gson().toJson(this)

fun Gson.fromJsonSet(json: String): HashSet<*>? = fromJson(json, HashSet::class.java)
