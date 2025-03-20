package dev.znci.rocket.util

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldType
import org.luaj.vm2.LuaTable

class RocketEnum(private val values: List<String>) {
    @Suppress("unused")
    fun getValues(): List<String> {
        return values
    }

    @Suppress("unused")
    fun isValid(key: String): Boolean {
        return values.contains(key)
    }

    fun getLuaTable(): LuaTable {
        val table = LuaTable()

        values.forEach() { value ->
            table.set(value, value)
        }

        return table
    }
}

fun <T : Enum<T>> enumToStringList(enumClass: Class<T>): List<String> {
    return enumClass.enumConstants.map { it.name }
}


data object RocketEnums {
    val RocketMaterial = RocketEnum(enumToStringList(Material::class.java))
    val RocketWorldType = RocketEnum(enumToStringList(WorldType::class.java))
    val RocketEnvironment = RocketEnum(enumToStringList(World.Environment::class.java))
}