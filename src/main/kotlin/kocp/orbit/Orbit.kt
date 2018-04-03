package kocp.orbit

import java.lang.Math.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class Orbit(val centerBody: CenterBody = CenterBody(),
            `semi-major axis`: Double = 0.0,
            eccentricity: Double = 0.0,
            inclination: Double = 0.0,
            `longitude of the ascending node`: Double = 0.0,
            `argument of periapsis`: Double = 0.0,
            `mean anomaly at epoch`: Double = 0.0) : Value() {
	
	constructor(
		ap: Double,
		pe: Double,
		centerBody: CenterBody,
		`location of pe`: Vector = Vector(1.0, 0.0, 0.0),
		`velocity of pe`: Vector = Vector(0.0, 1.0, 0.0))
		:
		this(
			centerBody = centerBody,
			`semi-major axis` = (ap + pe) / 2,
			eccentricity = if (ap === pe) 0.0 else pe * pe * `velocity of pe`.length() / centerBody.GM - 1,
			inclination = 0.0) {
		val range = (`location of pe` % `velocity of pe`)..Vector.Z
		inclination = PI / 2 -
			if (range > 0)
				range
			else
				-range
	}
	
	var `semi-major axis`: Double = `semi-major axis`
	
	var eccentricity: Double = eccentricity
		/**
		 * @param value Range of 0.0 to 1.0
		 * @throws EccentricityOutOfRangeException
		 */
		set(value) {
			if (value in 0.0..1.0) {
				field = value
			} else {
				throw EccentricityOutOfRangeException()
			}
		}
	
	var inclination: Double = inclination
		/**
		 * @param value Range of 0 to PI/2
		 * @throws InclinationOutOfRangeException
		 */
		set(value) {
			if (value in 0.0..(Math.PI / 2)) {
				field = value
			} else {
				throw InclinationOutOfRangeException()
			}
		}
	
	var `longitude of the ascending node`: Double = `longitude of the ascending node`
		/**
		 * @param value Range of -PI to PI
		 * @throws LongitudeOfTheAscendingNodeOutOfRangeException
		 */
		set(value) {
			if (value in -Math.PI..Math.PI) {
				field = value
			} else {
				throw LongitudeOfTheAscendingNodeOutOfRangeException()
			}
		}
	
	var `argument of periapsis`: Double = `argument of periapsis`
		/**
		 * @param value Range of -PI to PI
		 * @throwsArgumentOfPeriapsisOutOfRangeException
		 */
		set(value) {
			if (value in -Math.PI..Math.PI) {
				field = value
			} else {
				throw ArgumentOfPeriapsisOutOfRangeException()
			}
		}
	
	var `mean anomaly at epoch`: Double = `mean anomaly at epoch`
		/**
		 * @param value Range of 0 to PI*2
		 * @throwsArgumentOfPeriapsisOutOfRangeException
		 */
		set(value) {
			if (value in 0.0..(Math.PI * 2)) {
				field = value
			} else {
				throw MeanAnomalyAtEpochOutOfRangeException()
			}
		}
	
	var apogee: Double
		get() = `semi-major axis` * (1 + eccentricity)
		set(value) {
			//TODO
		}
	
	var perigee: Double
		get() = `semi-major axis` * (1 - eccentricity)
		set(value) {
			//TODO
		}
	
	//TODO
	var velocityOfApogee: Vector
		get() = Vector()
		set(value) {}
	
	//TODO
	var velocityOfPerigee: Vector
		get() = Vector()
		set(value) {}
	
	var e
		get() = eccentricity
		set(value) {
			eccentricity = value
		}
	
	val orbitOvalParameterA
		get() = (apogee + perigee) / 2
	
	
	val orbitOvalParameterB
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA * (1 - e * e))
	
	val orbitOvalParameterC
		get() = sqrt(orbitOvalParameterA * orbitOvalParameterA - orbitOvalParameterB * orbitOvalParameterB)
	
	val orbitOvalParameterE
		get() = eccentricity
	
	
	val orbitOvalParameterS
		get() = PI * orbitOvalParameterA * orbitOvalParameterA
	
	fun getH(range: Double): Double {
		val e = e
		return perigee * (1 + e) / (1 + e * cos(range))
	}
	
	fun getS(begin: Double, end: Double): Double {
		var t = if (end > begin) {
			Math.ceil(end - begin / Math.PI).toInt()
		} else {
			Math.floor(end - begin / Math.PI).toInt()
		}
		if (t != 0) t--
		
		val pe = perigee
		val e = e
		val a = 2 * sqrt((1 - e) * (1 - e) * (1 - e))
		val b = sqrt(1 + e)
		val c = sqrt(1 - e * e)
		val squareOfPe = pe * pe
		
		val cosBegin = cos(begin)
		val sinBegin = sin(begin)
		var sb = 0.0
		if (begin != 0.0)
			sb = -squareOfPe * b * (2 * atan(
				((-1 + e) * sinBegin) / (c * (1 + cosBegin))
			) * (1 + e * cosBegin) + e * c * sinBegin) / (a * (1 + e * cosBegin))
		
		val cosEnd = cos(end)
		val sinEnd = sin(end)
		var se = 0.0
		if (end != 0.0)
			se = -squareOfPe * b * (2 * atan(
				((-1 + e) * sinEnd) / (c * (1 + cosEnd))
			) * (1 + e * cosEnd) + e * c * sinEnd) / (a * (1 + e * cosEnd))
		if ((end - begin) / PI < 0)
			se += orbitOvalParameterS
		
		return se - sb + orbitOvalParameterS * t
	}
	
	class EccentricityOutOfRangeException(message: String? = null) : OutOfRangeException(message)
	class InclinationOutOfRangeException(message: String? = null) : OutOfRangeException(message)
	class LongitudeOfTheAscendingNodeOutOfRangeException(message: String? = null) : OutOfRangeException(message)
	class ArgumentOfPeriapsisOutOfRangeException(message: String? = null) : OutOfRangeException(message)
	class MeanAnomalyAtEpochOutOfRangeException(message: String? = null) : OutOfRangeException(message)
}
