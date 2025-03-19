package dev.znci.rocket.scripting

/**
 * Copyright 2025 znci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import kotlin.reflect.*
import kotlin.reflect.full.*
import org.luaj.vm2.*
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.JsePlatform

// XXX: Move to separate file
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LuaFunction

// XXX: Move to separate file
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class LuaProperty


abstract class RocketGlobal() : LuaTable() {

    init {
        registerFunctions(this)
        registerProperties(this)
    }

    fun getTable(): LuaTable {
        return this
    }

    private fun registerFunctions(table: LuaTable) {
        this::class.functions.forEach { function ->
            if (function.findAnnotation<LuaFunction>() != null) {
                println("registering function: ${function.name}")
                table.set(function.name, object : VarArgFunction() {
                    override fun invoke(args: Varargs): Varargs {
                        return try {
                            val kotlinArgs = args.toKotlinArgs(function)
                            val result = function.call(this@RocketGlobal, *kotlinArgs)
                            result.toLuaValue()
                        } catch (e: Exception) {
                            LuaValue.error("Error calling ${function.name}: ${e.message}")
                        }
                    }
                })
            }
        }
    }

    private fun registerProperties(table: LuaTable) {
        val properties = this::class.memberProperties
            .filter { it.findAnnotation<LuaProperty>() != null }

        // Handle property getting
        table.set("__index", object : TwoArgFunction() {
            override fun call(self: LuaValue, key: LuaValue): LuaValue {
                val prop = properties.find { it.name == key.tojstring() } ?: return LuaValue.NIL
                return try {
                    val value = prop.getter.call(this@RocketGlobal)
                    value.toLuaValue()
                } catch (e: Exception) {
                    LuaValue.error("Error getting '${prop.name}': ${e.message}")
                }
            }
        })

        // Handle property setting
        table.set("__newindex", object : ThreeArgFunction() {
            override fun call(self: LuaValue, key: LuaValue, value: LuaValue): LuaValue {
                val prop = properties.find { it.name == key.tojstring() } as? KMutableProperty<*>
                    ?: return LuaValue.error("No writable property '${key.tojstring()}'")
                return try {
                    prop.setter.call(this@RocketGlobal, value.toKotlinValue(prop.returnType.classifier))
                    LuaValue.TRUE
                } catch (e: Exception) {
                    LuaValue.error("Error setting '${prop.name}': ${e.message}")
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


class Test : RocketGlobal() {

    private var internalString = "Initial"

    @LuaProperty
    var stringRepresentation: String
        get() {
            println("Getter")
            return internalString
        }
        set(value) {
            println("Setter called with: $value")
            internalString = value.uppercase()  // just an example to showcase custom setter.
        }

    @LuaFunction
    fun testFunction(arg1: String): String {
        return "Hello, $arg1!"
    }
}



