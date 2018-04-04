package kocp

import kocp.math.UnitVector
import kocp.math.Vector

fun main(args: Array<String>) {
	val vector: Vector = UnitVector(1.0, 1.0, 1.0)
	println(vector)
	println(vector.length())
	vector.x = 1.0
	println(vector)
	println(vector.length())
}