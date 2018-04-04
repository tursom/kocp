package kocp.math

import kotlin.collections.HashSet

class HashSet<E>: HashSet<E>() {
	operator fun get(value:E): E {
		val index = indexOf(value)
		return if (index == -1) {
			add(value)
			value
		} else elementAt(index)
	}
}