package cn.tursom.kocp.math

open class Value {
	open class OutOfRangeException(message: String? = null) : Exception(message)
	open class AssignedToConstantException(message: String? = null) : Exception(message)
}