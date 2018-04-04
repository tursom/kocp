package kocp.math

val Double.square
	inline get() = this * this
val Double.sin
	inline get() = Math.sin(this)
val Double.cos
	inline get() = Math.cos(this)