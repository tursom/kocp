package cn.tursom.kocp.body

import cn.tursom.kocp.orbit.Orbit

class Ship(
	override val name: String,
	override val mass: Double,
	override val orbit: Orbit
) : Body {
	
	override fun toString() = "Ship(name=$name, mass=$mass, orbit=$orbit)"
}