package dev.znci.rocket.scripting.api

import dev.znci.rocket.scripting.annotations.RocketSimpleFunction
import dev.znci.rocket.scripting.annotations.RocketSimpleProperty
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import kotlin.reflect.KClassifier
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

abstract class RocketSimpleGlobal(name: String) : RocketGlobal(name) {

    init {
        registerFunctions(this.table)
        registerProperties(this.table)
    }

    private fun registerFunctions(table: LuaTable) {
        this::class.functions.forEach { function ->
            if (function.findAnnotation<RocketSimpleFunction>() != null) {
                println("registering function: ${function.name}")
                table.set(function.name, object : VarArgFunction() {
                    override fun invoke(args: Varargs): Varargs {
                        return try {
                            val kotlinArgs = args.toKotlinArgs(function)
                            val result = function.call(this@RocketSimpleGlobal, *kotlinArgs)
                            result.toLuaValue()
                        } catch (e: Exception) {
                            error("Error calling ${function.name}: ${e.message}")
                        }
                    }
                })
            }
        }
    }

    private fun registerProperties(table: LuaTable) {
        val properties = this::class.memberProperties
            .filter { it.findAnnotation<RocketSimpleProperty>() != null }

        // Handle property getting
        table.set("__index", object : TwoArgFunction() {
            override fun call(self: LuaValue, key: LuaValue): LuaValue {
                val prop = properties.find { it.name == key.tojstring() } ?: return NIL
                return try {
                    val value = prop.getter.call(this@RocketSimpleGlobal)
                    value.toLuaValue()
                } catch (e: Exception) {
                    error("Error getting '${prop.name}': ${e.message}")
                }
            }
        })

        // Handle property setting
        table.set("__newindex", object : ThreeArgFunction() {
            override fun call(self: LuaValue, key: LuaValue, value: LuaValue): LuaValue {
                val prop = properties.find { it.name == key.tojstring() } as? KMutableProperty<*>
                    ?: return error("No writable property '${key.tojstring()}'")
                return try {
                    prop.setter.call(this@RocketSimpleGlobal, value.toKotlinValue(prop.returnType.classifier))
                    TRUE
                } catch (e: Exception) {
                    error("Error setting '${prop.name}': ${e.message}")
                }
            }
        })
    }

    private fun Varargs.toKotlinArgs(func: KFunction<*>): Array<Any?> {
        val params = func.parameters.drop(1) // Skip `this`
        return params.mapIndexed { index, param ->
            this.arg(index + 1).toKotlinValue(param.type.classifier)
        }.toTypedArray()
    }

    private fun LuaValue.toKotlinValue(type: KClassifier?): Any? {
        return when (type) {
            String::class -> if (isnil()) null else tojstring()
            Boolean::class -> toboolean()
            Int::class -> toint()
            Double::class -> todouble()
            Float::class -> tofloat()
            else -> this
        }
    }

    private fun Any?.toLuaValue(): LuaValue {
        return when (this) {
            is String -> LuaValue.valueOf(this)
            is Boolean -> LuaValue.valueOf(this)
            is Int -> LuaValue.valueOf(this)
            is Double -> LuaValue.valueOf(this)
            //is Float -> LuaValue.valueOf(this) // FIXME: why do floats break the compiler ???
            else -> LuaValue.NIL
        }
    }
}