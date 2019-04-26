package cn.tursom.orbit

import cn.tursom.kocp.orbit.OvalOrbit
import org.junit.Test
import kotlin.math.PI

class OvalOrbitTest {
	private val orbit = OvalOrbit(200.0, 100.0)
	@Test
	fun testRiemannIntegral() {
		println(orbit)
		println(orbit.area)
		println(orbit.riemannIntegral(0.0, PI * 2))
		println(orbit.riemannIntegral(0.0, PI * 2 + 1))
		println(orbit.riemannIntegral(0.0, PI * 3))
	}
	
	@Test
	fun testGetArea() {
		println(orbit)
		for (i in 1..32) {
			println("i = $i")
			println(orbit.riemannIntegral(0.0, PI * 2 + i * 0.1))
			println(orbit.getArea(0.0, PI * 2 + i * 0.1))
			println(orbit.getArea(PI * 2 + i * 0.1, 0.0))
			println()
		}
	}
}