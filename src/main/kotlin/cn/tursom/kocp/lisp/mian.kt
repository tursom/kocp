package cn.tursom.kocp.lisp

class LanguageUnit(expression: String) {
	val operator: String
		get() = operatorType
	val args = ArrayList<LanguageUnit>()
	private var operatorType: String = ""

	init {
		var sb = StringBuilder()
		var bracketsCount = 0
		val args = ArrayList<String>()
		expression.replace("\\s+".toRegex(), " ").forEach {
			when (it) {
				'(' -> {
					when {
						bracketsCount == 0 -> {
							bracketsCount++
						}
						bracketsCount > 0 -> {
							bracketsCount++
							sb.append(it)
						}
						bracketsCount < 0 -> {
						}
					}
				}
				')' -> {
					when {
						bracketsCount == 0 -> {
							return@forEach
						}
						(bracketsCount == 1) || (bracketsCount == 2) -> {
							bracketsCount--
							if (sb.count() != 0) {
								if (sb[0] == '(') sb.append(')')
								args.add(sb.toString())
								sb = StringBuilder()
							}
						}
						bracketsCount > 2 -> {
							bracketsCount--
							sb.append(it)
						}
						bracketsCount < 0 -> {
							//TODO: throw a error
						}
					}
				}
				' ' -> {
					if (bracketsCount > 1) {
						sb.append(it)
					} else {
						args.add(sb.toString())
						sb = StringBuilder()
					}
				}
				else -> {
					if (bracketsCount == 0) {
						return@forEach
					} else {
						sb.append(it)
					}
				}
			}
		}
		if (sb.count() != 0) {
			args.add(sb.toString())
			sb = StringBuilder()
		}
		if (args.count() != 0) {
			operatorType = args[0]
			args.removeAt(0)
			args.forEach {
				this.args.add(LanguageUnit(it))
			}
		} else {
			operatorType = expression
		}
	}

	override fun toString(): String {
		return " $operator ${if (args.count() == 0) "" else args}"
	}
}

fun main(args: Array<String>) {
	println("[${LanguageUnit("(begin (define a 1)(add a 100)(print a))")}]")
//		.replace('[', '(').replace(']', ')')
//		.replace(" ",""))
}