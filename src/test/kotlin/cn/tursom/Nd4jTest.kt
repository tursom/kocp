package cn.tursom

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartFrame
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import org.nd4j.linalg.api.buffer.DataType
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms
import javax.swing.JFrame


class NDArray(
	@Suppress("MemberVisibilityCanBePrivate") val array: INDArray
) : INDArray by array {
	@Suppress("PropertyName")
	val T: INDArray
		get() = transpose()
	@Suppress("PropertyName")
	val Ti: INDArray
		get() = transposei()

	operator fun plus(other: INDArray): INDArray = add(other)
	operator fun plusAssign(other: INDArray) {
		addi(other)
	}

	operator fun plus(number: Number): INDArray = add(number)
	operator fun plusAssign(number: Number) {
		addi(number)
	}

	operator fun minus(other: INDArray): INDArray = sub(other)
	operator fun minusAssign(other: INDArray) {
		subi(other)
	}

	operator fun minus(number: Number): INDArray = sub(number)
	operator fun minusAssign(number: Number) {
		subi(number)
	}

	operator fun times(other: INDArray): INDArray = mul(other)
	operator fun timesAssign(other: INDArray) {
		muli(other)
	}

	operator fun times(number: Number): INDArray = mul(number)
	operator fun timesAssign(number: Number) {
		muli(number)
	}

	operator fun rem(other: INDArray): INDArray = mmul(other)
	operator fun remAssign(other: INDArray) {
		mmuli(other)
	}

	override operator fun div(other: INDArray): INDArray = array.div(other)
	operator fun divAssign(other: INDArray) {
		divi(other)
	}

	override operator fun div(number: Number): INDArray = array.div(number)
	operator fun divAssign(number: Number) {
		divi(number)
	}

	override fun toString(): String {
		return array.toString()
	}

	override fun hashCode(): Int {
		return array.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		return array == other
	}

	operator fun get(row: Long, column: Long) = array.getDouble(column, row)
}

fun main() {
	val nd = NDArray(Nd4j.linspace(0.0, Math.PI * 8, 1024, DataType.FLOAT))
	//println(nd)
	val sin = NDArray(Transforms.sin(nd))
	//println(sin)
	Transforms.abs(nd)
	val series = XYSeries("xySeries")
	for (i in 0 until nd.length()) {
		series.add(nd[1, i], sin[1, i])
	}

	val dataset = XYSeriesCollection()
	dataset.addSeries(series)
	val chart = ChartFactory.createXYLineChart(
		"y = sin x", // chart title
		"x", // x axis label
		"y", // y axis label
		dataset, // data
		PlotOrientation.VERTICAL,
		false, // include legend
		false, // tooltips
		false // urls
	)

	val frame = ChartFrame("my picture", chart)
	frame.pack()
	frame.isVisible = true
	frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}