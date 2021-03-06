package cn.tursom.database.sqlite

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

/*
 * SQLHelper，SQLite辅助使用类
 * 实现创建表格、查询、插入和更新功能
 */
class SQLHelper(private val connection: Connection) {
	init {
		connection.autoCommit = false
	}

	constructor(base: String)
			: this(DriverManager.getConnection("jdbc:sqlite:$base") ?: throw CantConnectDataBase())

	/*
	 * 创建表格
	 * table: 表格名
	 * keys: 属性列表
	 */
	fun createTable(table: String, keys: Array<String>) {
		val statement = connection.createStatement()
		statement.executeUpdate("CREATE TABLE if not exists $table (${toColumn(keys)})")
		commit()
	}

	/*
	 * 查询
	 * adapter: 用于保存查询结果的数据类，由SQLAdapter继承而来
	 * table: 表名
	 * name: 查询字段
	 * where: 指定从一个表或多个表中获取数据的条件,Pair左边为字段名，右边为限定的值
	 */
	fun <T : Any> select(
			adapter: SQLAdapter<T>, table: String,
			name: Array<String> = arrayOf("*"), where: Array<Pair<String, String>>? = null) {
		if (where != null) {
			select(adapter, table, toColumn(name), toWhere(where))
		} else {
			select(adapter, table, toColumn(name))
		}
	}

	fun <T : Any> select(
			adapter: SQLAdapter<T>, table: String, name: String = "*", where: String? = null
	) {
		val statement = connection.createStatement()
		adapter.adapt(
				if (where == null)
					statement.executeQuery("SELECT $name FROM $table ;")
				else
					statement.executeQuery("SELECT $name FROM $table WHERE $where;")
		)
		statement.closeOnCompletion()
	}

	/*
	 * 插入
	 * table: 表名
	 * column:
	 */
	fun insert(table: String, column: Array<Pair<String, String>>) {
		val columns = toKeys(column)
		insert(table, columns.first, columns.second)
	}

	fun insert(table: String, column: ArrayList<Pair<String, String>>) {
		val columns = toKeys(column.toTypedArray())
		insert(table, columns.first, columns.second)
	}

	fun insert(table: String, column: String, values: String) {
		val statement = connection.createStatement()
//		println("column:$column")
//		println("values:$values")
		val sql = "INSERT INTO $table ($column) VALUES ($values)"
		//println(sql)
		statement.executeUpdate(sql)
		commit()
		statement.closeOnCompletion()
	}

	fun update(
			table: String,
			set: Array<Pair<String, String>> = arrayOf(),
			where: Array<Pair<String, String>> = arrayOf()) {
		val statement = connection.createStatement()
		statement.executeUpdate("UPDATE $table SET ${toValue(set)} WHERE ${toWhere(where)};")
		commit()
		statement.closeOnCompletion()
	}

	private fun commit() {
		synchronized(connection) {
			connection.commit()
		}
	}

	fun close() {
		connection.close()
	}

	private fun toKeys(columns: Array<Pair<String, String>>): Pair<String, String> {
		val column = StringBuilder()
		val value = StringBuilder()
		columns.forEach {
			if (it.first.isNotEmpty() && it.second.isNotEmpty()) {
				column.append("${it.first},")
				value.append("'${it.second.replace("'", "''")}',")
			}
		}
		column.delete(column.length - 1, column.length)
		value.delete(value.length - 1, value.length)
		return Pair(column.toString(), value.toString())
	}

	private fun toColumn(column: Array<String>): String {
		val stringBuilder = StringBuilder()
		column.forEach {
			if (it.isNotEmpty())
				stringBuilder.append("$it,")
		}
		stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
		return stringBuilder.toString()
	}

	private fun toValue(where: Array<Pair<String, String>>): String {
		val stringBuilder = StringBuilder()
		where.forEach {
			if (it.first.isNotEmpty() && it.second.isNotEmpty())
				stringBuilder.append("${it.first}='${it.second.replace("'", "''")}',")
		}
		if (stringBuilder.isNotEmpty())
			stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
		return stringBuilder.toString()
	}

	private fun toWhere(where: Array<Pair<String, String>>): String {
		val stringBuilder = StringBuilder()
		where.forEach {
			if (it.first.isNotEmpty() && it.second.isNotEmpty())
				stringBuilder.append("${it.first}='${it.second.replace("'", "''")}' AND ")
		}
		if (stringBuilder.isNotEmpty())
			stringBuilder.delete(stringBuilder.length - 5, stringBuilder.length)
		return stringBuilder.toString()
	}

	class CantConnectDataBase(s: String? = null) : SQLException(s)
}