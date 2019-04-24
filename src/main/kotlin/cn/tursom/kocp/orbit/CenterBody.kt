package cn.tursom.kocp.orbit

import cn.tursom.kocp.math.Value
import cn.tursom.kocp.math.toJson
import cn.tursom.kocp.math.fromJsonSet
import cn.tursom.kocp.math.get
import com.google.gson.Gson

class CenterBody(GM: Double? = null, mass: Double? = null, radius: Double = 0.0, val name: String? = null) : Value {
	val GM: Double = GM ?: (mass ?: 0.0) * G
	
	val mass: Double
		inline get() = GM / G
	
	val radius: Double =
		if (radius >= 0) {
			radius
		} else {
			throw RadiusOutOfRangeException()
		}
	
	class RadiusOutOfRangeException(message: String? = null) : Value.OutOfRangeException(message)
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as CenterBody
		
		if (name != other.name) return false
		if (GM != other.GM) return false
		if (radius != other.radius) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = name?.hashCode() ?: 0
		result = 31 * result + GM.hashCode()
		result = 31 * result + radius.hashCode()
		return result
	}
	
	override fun toString() =
		if (name != null) "CenterBody(name=$name, GM=$GM, radius=$radius)"
		else "CenterBody(GM=$GM, radius=$radius)"
	
	companion object {
		const val G: Double = 6.67408e-11
		
		private val centerBodyTable = HashSet<CenterBody>()
		
		override fun toString(): String {
			return centerBodyTable.toString()
		}
		
		val names: String?
			get() {
				val maps = HashSet<String>()
				centerBodyTable.forEach {
					maps.add(it.name ?: "")
				}
				return maps.toJson()
			}
		
		val json
			get() = this.toJson()
		
		fun toJson(): String? {
			return centerBodyTable.toJson()
		}
		
		fun fromJson(json: String) {
			Gson().fromJsonSet(json)?.forEach {
				try {
					centerBodyTable.add(it as CenterBody)
				} catch (e: Exception) {
				}
			}
		}
		
		val Earth = CenterBody(mass = 5.97237e24, radius = 6.371e6, name = "Earth")
		
		operator fun get(centerBody: CenterBody): CenterBody {
			return centerBodyTable[centerBody]
		}
	}
}

