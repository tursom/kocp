package kocp.orbit

import com.google.gson.Gson
import kocp.math.*
import kocp.orbit.CenterBody.Companion.Earth
import kotlin.math.*

class Orbit(apogee: Double = 0.0,
            perigee: Double = 0.0,
            var range: Double = 0.0,
            centerBody: CenterBody = Earth,
            `location of pe`: Vector = Vector(1.0, 0.0, 0.0),
            `velocity of pe`: Vector = Vector(0.0, 1.0, 0.0)) : Value() {

	constructor(json: String) : this(Orbit.fromJson(json)!!)
	constructor(orbit: Orbit) : this(orbit.apogee, orbit.perigee, orbit.range, orbit.centerBody, orbit.locationOfPe, orbit.velocityOfPerigee)

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

	val centerBody = CenterBody[centerBody]

	var locationOfPe: Vector = `location of pe`

	var velocityOfPerigee: Vector = `velocity of pe`

	val e
		get() = orbitOvalParameterE

	val area
		get() = PI * orbitOvalParameterA * orbitOvalParameterB

	val high
		get() = getHigh(range)

	val orbitOvalParameterA
		get() = (apogee + perigee) / 2


	val orbitOvalParameterB
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA * (1 - e * e))

	val orbitOvalParameterC
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA - orbitOvalParameterB * orbitOvalParameterB)

	val orbitOvalParameterE
		get() = if (apogee == perigee) 0.0 else perigee * velocityOfPerigee.lengthSquare() / centerBody.GM - 1


	val orbitOvalParameterS
		get() = area

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
	 *     (
	 *       (2-2*e)*sin(x)^2
	 *       +(2*e+2)*cos(x)^2
	 *       +(4*e+4)*cos(x)
	 *       +2*e+2)*atan(
	 *         (
	 *           (e-1)*sin(x)
	 *         )/(
	 *           sqrt(1-e)*sqrt(e+1)*(cos(x)+1)
	 *         )
	 *       )
	 *       +sqrt(1-e)*sqrt(e+1)*(2*e*cos(x)
	 *       +2*e
	 *     )*sin(x)
	 *   )/(
	 *     sqrt(1-e)*sqrt(e+1)*(
	 *       (e^3-e^2-e+1)*sin(x)^2
	 *       +(
	 *         (-e^3)-e^2+e+1
	 *       )*cos(x)^2
	 *       +(
	 *         (-2*e^3)-2*e^2+2*e+2
	 *       )*cos(x)
	 *       -e^3-e^2+e+1
	 *     )
	 *   )
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
	 *      2 * pow((1 - e), 1.5) * ( 1 + e * cos(x) )
	 *    )
	 *
	 *  即为本算法所用公式
	 */
	fun getArea(begin: Double = 0.0, end: Double = 0.0): Double {
		val t = if (end > begin) {
			floor((end - begin) / (PI * 2)).toInt()
		} else {
			ceil((end - begin) / (PI * 2)).toInt()
		}

		val e = e
		val a = 2 * (1 - e).pow(1.5)
		val b = sqrt(1 + e)
		val c = sqrt(1 - e * e)

		val cosBegin = begin.cos
		val sinBegin = begin.sin
		val areaBegin =
			if ((begin % (2 * PI)) != 0.0)
				-perigee.square * b * (2 * atan(
					(-1 + e) * sinBegin / (c * (1 + cosBegin))
				) * (1 + e * cosBegin)
					+ e * c * sinBegin) / (a * (1 + e * cosBegin))
			else 0.0

		val cosEnd = end.cos
		val sinEnd = end.sin
		val areaEnd =
			if ((end % (2 * PI)) != 0.0)
				-perigee.square * b * (2 * atan(
					(-1 + e) * sinEnd / (c * (1 + cosEnd))
				) * (1 + e * cosEnd)
					+ e * c * sinEnd) / (a * (1 + e * cosEnd))
			else 0.0

		return areaEnd - areaBegin + orbitOvalParameterS * t
	}

	fun toJson() = Gson().toJson(this) ?: "{}"

	private fun checkApogeeAndPerigee() {
		if (apogee < perigee) {
			val swap = apogee
			apogee = perigee
			perigee = swap
			velocityOfPerigee.negative()
			locationOfPe.negative()
		}
	}

	override fun toString(): String {
		return "Orbit(range=$range, centerBody=$centerBody, apogee=$apogee, perigee=$perigee, locationOfPe=$locationOfPe, velocityOfPerigee=$velocityOfPerigee)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Orbit

		if (range != other.range) return false
		if (centerBody != other.centerBody) return false
		if (apogee != other.apogee) return false
		if (perigee != other.perigee) return false
		if (locationOfPe != other.locationOfPe) return false
		if (velocityOfPerigee != other.velocityOfPerigee) return false

		return true
	}

	override fun hashCode(): Int {
		var result = range.hashCode()
		result = 31 * result + centerBody.hashCode()
		result = 31 * result + apogee.hashCode()
		result = 31 * result + perigee.hashCode()
		result = 31 * result + locationOfPe.hashCode()
		result = 31 * result + velocityOfPerigee.hashCode()
		return result
	}

	companion object {
		fun fromJson(json: String) = Gson().fromJson(json, Orbit::class.java) ?: null
	}
}
