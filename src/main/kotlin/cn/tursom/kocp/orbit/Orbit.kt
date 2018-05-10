package cn.tursom.kocp.orbit

import com.google.gson.Gson
import cn.tursom.kocp.math.*
import cn.tursom.kocp.orbit.CenterBody.Companion.Earth
import kotlin.math.*

/**
 * 用于描述一个天体的运行轨道的类
 */

class Orbit(apogee: Double = 0.0,
            perigee: Double = 0.0,
            var range: Double = 0.0,
            centerBody: CenterBody = Earth,
            `location of pe`: Vector = Vector(1.0, 0.0, 0.0),
            `velocity of pe`: Vector = Vector(0.0, 1.0, 0.0)) : Value() {

	constructor(json: String) : this(Orbit.fromJson(json)!!)
	constructor(orbit: Orbit) : this(
		orbit.apogee,
		orbit.perigee,
		orbit.range,
		orbit.centerBody,
		orbit.locationOfPerigee,
		orbit.velocityOfPerigee)

	/**
	 * 远地点高度（地心起算）
	 */
	var apogee: Double = apogee
		set(value) {
			field = value
			checkApogeeAndPerigee()
		}

	/**
	 * 近地点高度（地心起算）
	 */
	var perigee: Double = perigee
		set(value) {
			field = value
			checkApogeeAndPerigee()
		}

	/**
	 * 中心天体
	 */
	val centerBody = CenterBody[centerBody]

	/**
	 * 远地点位置矢量
	 */
	val locationOfApogee: Vector
		get() = -locationOfPerigee.unit * apogee

	/**
	 * 近地点位置矢量
	 */
	var locationOfPerigee: Vector = `location of pe`

	/**
	 * 近地点速度矢量
	 */
	var velocityOfPerigee: Vector = `velocity of pe`

	/**
	 * 椭圆轨道偏心率
	 */
	val e
		get() = orbitOvalParameterE

	/**
	 * 轨道面积（如果存在）
	 */
	val area
		get() = PI * orbitOvalParameterA * orbitOvalParameterB

	/**
	 * 现时高度
	 */
	val high
		get() = getHigh(range)

	/**
	 * 椭圆轨道参数a(半长轴)
	 */
	val orbitOvalParameterA
		get() = (apogee + perigee) / 2

	/**
	 * 椭圆轨道参数b(半短轴)
	 */
	val orbitOvalParameterB
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA * (1 - e * e))

	/**
	 * 椭圆轨道参数c(半焦距)
	 */
	val orbitOvalParameterC
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA - orbitOvalParameterB * orbitOvalParameterB)

	/**
	 * 椭圆轨道参数e(偏心率)
	 */
	val orbitOvalParameterE
		get() =
			if (apogee == perigee)
				0.0
			else
				perigee * velocityOfPerigee.lengthSquare() / centerBody.GM - 1

	/**
	 * 椭圆轨道参数S(面积)
	 */
	val orbitOvalParameterS
		get() = area

	/**
	 * 获取从近地点运行所给角度时的高度
	 */
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
	 *      2 * (1 - e) ^ 1.5 * ( 1 + e * cos(x) )
	 *    )
	 *
	 *  即为本算法所用公式
	 */
	fun getArea(begin: Double = 0.0, end: Double = 0.0): Double {
		/**
		 * https://zblog.tursom.cn/zb_users/upload/2018/04/201804201524189148271128.png
		 * 该函数是周期函数，所以需要预先计算出循环周期
		 */
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
			locationOfPerigee.negative()
		}
	}

	override fun toString() =
		"Orbit(range=$range, centerBody=$centerBody, apogee=$apogee, perigee=$perigee, " +
			"locationOfPerigee=$locationOfPerigee, velocityOfPerigee=$velocityOfPerigee)"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Orbit

		if (range != other.range) return false
		if (centerBody != other.centerBody) return false
		if (apogee != other.apogee) return false
		if (perigee != other.perigee) return false
		if (locationOfPerigee != other.locationOfPerigee) return false
		if (velocityOfPerigee != other.velocityOfPerigee) return false

		return true
	}

	override fun hashCode(): Int {
		var result = range.hashCode()
		result = 31 * result + centerBody.hashCode()
		result = 31 * result + apogee.hashCode()
		result = 31 * result + perigee.hashCode()
		result = 31 * result + locationOfPerigee.hashCode()
		result = 31 * result + velocityOfPerigee.hashCode()
		return result
	}

	companion object {
		fun fromJson(json: String) = Gson().fromJson(json, Orbit::class.java) ?: null
	}
}
