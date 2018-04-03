class Orbit(centerBody: CenterBody = CenterBody(),
            `semi-major axis`: Double = 0.0,
            eccentricity: Double = 0.0,
            inclination: Double = 0.0,
            `longitude of the ascending node`: Double = 0.0,
            `argument of periapsis`: Double = 0.0,
            `mean anomaly`: Double = 0.0) {
	
	//TODO
	constructor(ae: Double, pe: Double, centerBody: CenterBody, `velocity of ae`: Vector, `velocity of pe`: Vector) : this() {}
	
	val centerBody: CenterBody = centerBody
	
	var `semi-major axis`: Double = `semi-major axis`
	var eccentricity: Double = eccentricity
		set(value) {
			if (value in 0.0..1.0) {
				field = value
			} else {
				throw EccentricityOutOfRangeException()
			}
		}
	var inclination: Double = inclination
	var `longitude of the ascending node`: Double = `longitude of the ascending node`
	var `argument of periapsis`: Double = `argument of periapsis`
	var `mean anomaly`: Double = `mean anomaly`
	
	//TODO
	var ae: Double
		get() = 0.0
		set(value) {}
	
	//TODO
	var pe: Double
		get() = 0.0
		set(value) {}
	
	//TODO
	var `velocity of ae`: Vector
		get() = Vector()
		set(value) {}
	
	//TODO
	var `velocity of pe`: Vector
		get() = Vector()
		set(value) {}
	
	class EccentricityOutOfRangeException(message: String? = null) : Exception(message)
}