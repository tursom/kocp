package cn.tursom.kocp.math

interface Value {
	open class OutOfRangeException(message: String? = null) : Exception(message)
	open class AssignedToConstantException(message: String? = null) : Exception(message)
}