package kocp.orbit

import kocp.math.Value
import kocp.math.Vector
import java.lang.Math.*


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class Orbit(apogee: Double = 0.0,
            perigee: Double = 0.0,
            range: Double = 0.0,
            val centerBody: CenterBody = CenterBody(),
            `location of pe`: Vector = Vector(1.0, 0.0, 0.0),
            `velocity of pe`: Vector = Vector(0.0, 1.0, 0.0)) : Value() {
	
	var apogee: Double = apogee
		set(value) {
			field = value
			checkApogeeAndPerigee()
		}
	
	var perigee: Double = perigee
		set(value) {
			field = value
			checkApogeeAndPerigee()
		}
	
	var locationOfPe: Vector = `location of pe`
		set(value) {}
	
	var velocityOfPerigee: Vector = `velocity of pe`
		set(value) {}
	
	val e
		inline get() = orbitOvalParameterE
	
	val orbitOvalParameterA
		get() = (apogee + perigee) / 2
	
	
	val orbitOvalParameterB
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA * (1 - e * e))
	
	val orbitOvalParameterC
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA - orbitOvalParameterB * orbitOvalParameterB)
	
	val orbitOvalParameterE
		get() = if (apogee == perigee) 0.0 else perigee * velocityOfPerigee.lengthSquare() / centerBody.GM - 1
	
	
	val orbitOvalParameterS
		get() = PI * orbitOvalParameterA * orbitOvalParameterB
	
	fun getHigh(range: Double): Double {
		val e = e
		return perigee * (1 + e) / (1 + e * cos(range))
	}
	
	/**
	 * 轨道椭圆的极坐标方程为
	 * r = (
	 *       ( 1 + e ) * perigee
	 *     )/(
	 *       1 + e * cos ( x )
	 *     )
	 * ，其中x为角度
	 * 对其做不定积分，得
	 *  -(
	 *    (
	 *      (2-2*e)*sin(x)^2
	 *      +(2*e+2)*cos(x)^2
	 *      +(4*e+4)*cos(x)
	 *      +2*e+2)*atan(
	 *        (
	 *          (e-1)*sin(x)
	 *        )/(
	 *          sqrt(1-e)*sqrt(e+1)*(cos(x)+1)
	 *        )
	 *      )
	 *      +sqrt(1-e)*sqrt(e+1)*(2*e*cos(x)
	 *      +2*e
	 *    )*sin(x)
	 *  )/(
	 *    sqrt(1-e)*sqrt(e+1)*(
	 *      (e^3-e^2-e+1)*sin(x)^2
	 *      +(
	 *        (-e^3)-e^2+e+1
	 *      )*cos(x)^2
	 *      +(
	 *        (-2*e^3)-2*e^2+2*e+2
	 *      )*cos(x)
	 *      -e^3-e^2+e+1
	 *    )
	 *  )
	 *  * ( 1 + e) * ( 1 + e) * perigee * perigee / 2
	 *
	 *  化简得
	 *  - perigee * perigee * sqrt(1 + e) * (
	 *
	 *      2 * atan(
	 *                ( -1 + e )
	 *                * sin(x)
	 *                / (
	 *                    sqrt( 1 - e * e ) * ( 1 + cos(x) )
	 *                  )
	 *              ) * (1 + e * cos(x) )
	 *      + e * sqrt(1 - e * e) * sin(x)
	 *
	 *    ) / (
	 *          2 * sqrt(
	 *                    (1 - e) * (1 - e) * (1 - e)
	 *                  ) * ( 1 + e * cos(x) )
	 *        )
	 *
	 *  即为本算法所用公式
	 */
	fun getArea(begin: Double, end: Double): Double {
		val t = if (end > begin) {
			ceil((end - begin) / (PI * 2)).toInt()
		} else {
			floor((end - begin) / (PI * 2)).toInt()
		}
		//if (t != 0) t--
		
		val e = e
		val a = 2 * sqrt((1 - e) * (1 - e) * (1 - e))
		val b = sqrt(1 + e)
		val c = sqrt(1 - e * e)
		val peSquare = perigee * perigee
		
		val cosBegin = cos(begin)
		val sinBegin = sin(begin)
		val areaBegin =
			if (begin != 0.0)
				-peSquare * b * (2 * atan(
					(-1 + e) * sinBegin / (c * (1 + cosBegin))
				) * (1 + e * cosBegin) + e * c * sinBegin) / (a * (1 + e * cosBegin))
			else 0.0
		
		val cosEnd = cos(end)
		val sinEnd = sin(end)
		val areaEnd =
			if (end != 0.0)
				-peSquare * b * (2 * java.lang.Math.atan(
					(-1 + e) * sinEnd / (c * (1 + cosEnd))
				) * (1 + e * cosEnd) + e * c * sinEnd) / (a * (1 + e * cosEnd))
			else 0.0
		
		return areaEnd - areaBegin + orbitOvalParameterS * t
	}
	
	private fun checkApogeeAndPerigee() {
		if (apogee < perigee) {
			val swap = apogee
			apogee = perigee
			perigee = swap
		}
	}
	
	companion object {
		private fun Vector.acuteAngle(vector: Vector): Double {
			val range = this..vector
			return if (range < PI / 2) range else PI - range
		}
	}
}
