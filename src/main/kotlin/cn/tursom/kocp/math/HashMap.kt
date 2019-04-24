package cn.tursom.kocp.math

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlin.collections.HashMap

fun HashMap<*, *>.toJson() = Gson().toJson(this) ?: "{}"

fun Gson.fromJsonMap(json: String): HashMap<*, *>? {
	return fromJson(json, HashMap::class.java)
}
