package cn.tursom.kocp.orbit

import cn.tursom.kocp.math.Value

interface Orbit : Value{
	/**
	 * 获取从近地点运行所给角度时的高度
	 */
	fun getHigh(range: Double): Double
	
	/**
	 * 获取从近地点起算的起始角度到终止角度的面积
	 */
	fun getArea(begin: Double = 0.0, end: Double = 0.0): Double
}