package dev.znci.rocket.scripting.api

open class RocketEnum(
    override var valueName: String = ""
) : RocketTable("") {
    private val enumValues = mutableMapOf<String, Any>()

    fun set(values: Map<String, Any>) {
        enumValues.clear()
        enumValues.putAll(values)
    }

    fun <T : Enum<T>> set(enumClass: Class<T>) {
        enumValues.clear()
        for (enumConstant in enumClass.enumConstants) {
            enumValues[enumConstant.name] = enumConstant
        }
    }

    fun addValue(key: String, value: Any) {
        enumValues[key] = value
    }

    fun getValues(): Map<String, Any> {
        return enumValues
    }

    fun isValidKey(key: Any): Boolean {
        return enumValues.containsKey(key)
    }

    fun getValue(key: String): Any? {
        return enumValues[key]
    }

    fun convertToLuaTable(): RocketTable {
        val table = RocketTable("")
        for ((key, value) in enumValues) {
            try {
                if (key.toString() == value.toString()) {
                    val valueTable = RocketTable("")
                    valueTable.setSimple("enumName", key)
                    valueTable.setSimple("enumType", value.javaClass.simpleName)
                    valueTable.setSimple("enumValue", value.toString())
                    valueTable.setSimple("isEnum", true)
                    table.setSimple(key, valueTable)
                } else {
                    table.setSimple(key, value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return table
    }
}
