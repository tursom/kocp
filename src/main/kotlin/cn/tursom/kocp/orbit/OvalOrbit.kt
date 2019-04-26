package cn.tursom.kocp.orbit

import cn.tursom.kocp.body.Body
import cn.tursom.kocp.body.CenterBody.Companion.Earth
import cn.tursom.kocp.math.*
import com.google.gson.Gson
import kotlin.math.*

/**
 * 用于描述一个天体的运行轨道的类
 */

@Suppress("MemberVisibilityCanBePrivate", "unused")
class OvalOrbit(
	/**
	 * 远地点高度（地心起算）
	 */
	val apogee: Double = 0.0,
	
	/**
	 * 近地点高度（地心起算）
	 */
	val perigee: Double = 0.0,
	
	/**
	 * 当前位置经过角度（近地点起算）
	 */
	val range: Double = 0.0,
	
	/**
	 * 中心天体
	 */
	val centerBody: Body = Earth,
	
	/**
	 * 近地点位置单位矢量
	 */
	val locationOfPerigee: UnitVector = UnitVector(1.0, 0.0, 0.0),
	
	/**
	 * 近地点速度单位矢量
	 */
	val velocityOfPerigee: UnitVector = UnitVector(0.0, 1.0, 0.0)
) : Orbit {
	
	/**
	 * 椭圆轨道参数a(半长轴)
	 */
	val orbitOvalParameterA
		get() = (apogee + perigee) / 2
	
	/**
	 * 椭圆轨道参数e(偏心率)
	 */
	val orbitOvalParameterE
		get() =
			if (apogee == perigee) {
				0.0
			} else {
				(apogee - perigee) / 2 / orbitOvalParameterA
			}
	
	/**
	 * 椭圆轨道参数b(半短轴)
	 */
	val orbitOvalParameterB
		get() = sqrt(orbitOvalParameterA.square * (1 - orbitOvalParameterE.square))
	
	/**
	 * 椭圆轨道参数c(半焦距)
	 */
	val orbitOvalParameterC
		get() = (apogee - perigee) / 2
	
	/**
	 * 轨道面积（如果存在）
	 */
	val area = PI * orbitOvalParameterA * orbitOvalParameterB
	
	/**
	 * 椭圆轨道参数S(面积)
	 */
	val orbitOvalParameterS = area
	
	
	/**
	 * 椭圆轨道参数a(半长轴)
	 */
	val a = orbitOvalParameterA
	
	/**
	 * 椭圆轨道参数b(半短轴)
	 */
	val b = orbitOvalParameterB
	
	/**
	 * 椭圆轨道参数c(半焦距)
	 */
	val c = orbitOvalParameterC
	
	/**
	 * 椭圆轨道偏心率
	 */
	val e = orbitOvalParameterE
	
	
	/**
	 * 现时高度
	 */
	val high = getHigh(range)
	
	/**
	 * 获取从近地点运行所给角度时的高度
	 */
	override fun getHigh(range: Double) = perigee * (1 + e) / (1 + e * cos(range))
	
	// 三个加速计算变量
	private val getAreaA = 2 * (1 - e).pow(1.5)
	private val getAreaB = sqrt(1 + e)
	private val getAreaC = sqrt(1 - e * e)
	
	/**
	 * 轨道椭圆的极坐标方程为
	 * r = (
	 *       ( 1 + E ) * perigee
	 *     )/(
	 *       1 + E * cos ( x )
	 *     )
	 * 其中x为角度，E为轨道的离心率
	 *
	 * 对其做不定积分，得
	 *  -(
	 *     (
	 *       (2-2*E)*sin(x)^2
	 *       +(2*E+2)*cos(x)^2
	 *       +(4*E+4)*cos(x)
	 *       +2*E+2)*atan(
	 *         (
	 *           (E-1)*sin(x)
	 *         )/(
	 *           sqrt(1-E)*sqrt(E+1)*(cos(x)+1)
	 *         )
	 *       )
	 *       +sqrt(1-E)*sqrt(E+1)*(2*E*cos(x)
	 *       +2*E
	 *     )*sin(x)
	 *   )/(
	 *     sqrt(1-E)*sqrt(E+1)*(
	 *       (E^3-E^2-E+1)*sin(x)^2
	 *       +(
	 *         (-E^3)-E^2+E+1
	 *       )*cos(x)^2
	 *       +(
	 *         (-2*E^3)-2*E^2+2*E+2
	 *       )*cos(x)
	 *       -E^3-E^2+E+1
	 *     )
	 *   )
	 *  * ( 1 + E) * ( 1 + E) * perigee * perigee / 2
	 *
	 *  化简得
	 *  - perigee * perigee * sqrt(1 + E) * (
	 *
	 *      2 * atan(
	 *                ( -1 + E )
	 *                * sin(x)
	 *                / (
	 *                    sqrt( 1 - E * E ) * ( 1 + cos(x) )
	 *                  )
	 *              ) * (1 + E * cos(x) )
	 *      + E * sqrt(1 - E * E) * sin(x)
	 *
	 *    ) / (
	 *      2 * (1 - E) ^ 1.5 * ( 1 + E * cos(x) )
	 *    )
	 *
	 *  即为本算法所用公式
	 */
	override fun getArea(begin: Double, end: Double): Double {
		if (begin == end) return 0.0
		
		/*
		 * https://zblog.tursom.cn/zb_users/upload/2018/04/201804201524189148271128.png
		 * 该函数是周期函数，所以需要预先计算出循环周期
		 */
		val t = if (end > begin) {
			(end - begin + PI) / (PI * 2)
		} else {
			-(begin - end + PI) / (PI * 2)
		}.toInt()
		
		val cosBegin = begin.cos
		val sinBegin = begin.sin
		val areaBegin =
			if ((begin % (2 * PI)) != 0.0)
				-perigee.square * getAreaB * (2 * atan(
					(-1 + e) * sinBegin / (getAreaC * (1 + cosBegin))
				) * (1 + e * cosBegin)
					+ e * getAreaC * sinBegin) / (getAreaA * (1 + e * cosBegin))
			else 0.0
		
		val cosEnd = end.cos
		val sinEnd = end.sin
		val areaEnd =
			if ((end % (2 * PI)) != 0.0)
				-perigee.square * getAreaB * (2 * atan(
					(-1 + e) * sinEnd / (getAreaC * (1 + cosEnd))
				) * (1 + e * cosEnd)
					+ e * getAreaC * sinEnd) / (getAreaA * (1 + e * cosEnd))
			else 0.0
		
		return areaEnd - areaBegin + orbitOvalParameterS * t
	}
	
	fun riemannIntegral(begin: Double = 0.0, end: Double = 0.0): Double {
		val loop = ((end - begin) / PI).toInt()
//		val loop = 0
		val en = end - loop * PI
		var s = 0.0
		var p = begin
		val dp = if (en > begin) 0.001 else -0.001
		while (p < en) {
			s += getHigh(p).square * dp / 2
			p += dp
		}
		return loop * area / 2 + s
	}
	
	fun toJson() = Gson().toJson(this) ?: "{}"
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as OvalOrbit
		
		if (apogee != other.apogee) return false
		if (perigee != other.perigee) return false
		if (range != other.range) return false
		if (centerBody != other.centerBody) return false
		if (locationOfPerigee != other.locationOfPerigee) return false
		if (velocityOfPerigee != other.velocityOfPerigee) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = apogee.hashCode()
		result = 31 * result + perigee.hashCode()
		result = 31 * result + range.hashCode()
		result = 31 * result + centerBody.hashCode()
		result = 31 * result + locationOfPerigee.hashCode()
		result = 31 * result + velocityOfPerigee.hashCode()
		return result
	}
	
	override fun toString(): String {
		return "OvalOrbit(apogee=$apogee, perigee=$perigee, locationOfPerigee=$locationOfPerigee, velocityOfPerigee=$velocityOfPerigee, centerBody=$centerBody, a=$a, b=$b, c=$c, e=$e, area=$area, range=$range, high=$high)"
	}
}
