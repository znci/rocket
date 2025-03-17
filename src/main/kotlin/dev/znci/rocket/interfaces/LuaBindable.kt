package dev.znci.rocket.interfaces // XXX: This is not an interface.

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
import org.luaj.vm2.*
import org.luaj.vm2.lib.*
import java.util.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

abstract class LuaBindable(obj: Any?) : LuaUserdata(obj) {
    init {
        val meta = LuaTable()

        // Automatic property getter with support for custom getters
        meta.set("__index", object : TwoArgFunction() {
            override fun call(self: LuaValue, key: LuaValue): LuaValue {
                val propertyName = key.tojstring()

                // Check for explicit getter method
                val getter = this@LuaBindable::class.memberFunctions.find { it ->
                    it.name == "get${propertyName.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }}" }
                if (getter != null) {
                    getter.isAccessible = true
                    return getter.call(this@LuaBindable)?.toLuaValue() ?: LuaValue.NIL
                }

                // Check for a property directly
                val property = this@LuaBindable::class.memberProperties.find { it.name == propertyName }
                property?.isAccessible = true
                return property?.getter?.call(this@LuaBindable)?.toLuaValue() ?: LuaValue.NIL
            }
        })

        // Automatic property setter with support for custom setters
        meta.set("__newindex", object : ThreeArgFunction() {
            override fun call(self: LuaValue, key: LuaValue, value: LuaValue): LuaValue {
                val propertyName = key.tojstring()

                // Check for explicit setter method
                val setter = this@LuaBindable::class.memberFunctions.find { it ->
                    it.name == "set${propertyName.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }}" }
                if (setter != null && setter.parameters.size == 2) { // Expecting one argument
                    setter.isAccessible = true
                    setter.call(this@LuaBindable, value.toKotlinValue(setter.parameters[1].type.classifier))
                    return LuaValue.NIL
                }

                // Check for a mutable property
                val property = this@LuaBindable::class.memberProperties.find { it.name == propertyName }
                if (property is kotlin.reflect.KMutableProperty<*>) {
                    property.setter.call(this@LuaBindable, value.toKotlinValue(property.returnType.classifier))
                }
                return LuaValue.NIL
            }
        })

        // Automatic method calling
        meta.set("__call", object : VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                val methodName = args.arg1().tojstring()
                val method = this@LuaBindable::class.memberFunctions.find { it.name == methodName }
                method?.isAccessible = true

                return method?.call(
                    this@LuaBindable,
                    *args.subargs(2).toKotlinArgs(method.parameters.drop(1))
                )?.toLuaValue() ?: LuaValue.NIL
            }
        })


        @Suppress("LeakingThis")
        setmetatable(meta) // TODO: Find a better way to fix this.
                           //       The suppression is needed because we are calling a method on a class that is not fully initialized.
                           //       This is a common problem with Kotlin and we have to find a better way to fix this.
    }
}

fun Any?.toLuaValue(): LuaValue = when (this) {
    is Number -> LuaValue.valueOf(toDouble())
    is String -> LuaValue.valueOf(this)
    is Boolean -> LuaValue.valueOf(this)
    is LuaBindable -> this
    else -> LuaValue.NIL
}

fun LuaValue.toKotlinValue(type: Any?): Any? = when (type) {
    Double::class, Float::class -> todouble()
    Int::class, Long::class -> toint()
    Boolean::class -> toboolean()
    String::class -> tojstring()
    else -> this
}

fun Varargs.toKotlinArgs(paramTypes: List<kotlin.reflect.KParameter>): Array<Any?> {
    return paramTypes.mapIndexed { index, param ->
        this.arg(index + 1).toKotlinValue(param.type.classifier)
    }.toTypedArray()
}

