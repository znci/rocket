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

package dev.znci.rocket.scripting.functions

import com.dieselpoint.norm.Database
import dev.znci.rocket.data.DataManager
import dev.znci.rocket.data.models.Variable
import dev.znci.rocket.interfaces.LuaBindable
import dev.znci.rocket.interfaces.Storable
import dev.znci.rocket.util.LuaValueHelper
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class LuaVariables : LuaBindable(null) {
    private var db: Database = DataManager.getDatabase()

    // Lua-accessible methods

    fun getVar(key: String): LuaValue {
        val variable = getVariable(key)
        return if (variable != null) {
            org.luaj.vm2.lib.jse.CoerceJavaToLua.coerce(variable)
        } else {
            LuaValue.NIL
        }
    }

    fun setVar(variableKey: String, value: LuaValue): LuaValue {
        val tableData = value.checktable()
        val className = tableData.get("_javaClass").tojstring()
        val valueHelper = LuaValueHelper()

        try {
            // Get the Class object dynamically
            val clazz = Class.forName(className).kotlin

            // Generate dummy values for constructor parameters
            val constructor = clazz.primaryConstructor ?: return LuaValue.NIL

            // Get arg names for constructor (list of strings)
            val argNames = constructor.parameters.map { it.name }

            // Get arg types for constructor (list of KClasses)
            val argTypes = constructor.parameters.map { it.type.jvmErasure }

            // Get arg values from tableData
            val argsArray = argNames.map { argName ->
                val luaValue = tableData.get(argName)
                valueHelper.parseLuaValue(luaValue, argTypes[argNames.indexOf(argName)])
            }.toTypedArray()

            // Create an instance with data from tableData
            val instance = constructor.call(*argsArray)

            // Save the instance to the database
            setVariable(variableKey, instance as Storable)

            return LuaValue.userdataOf(instance)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return LuaValue.NIL
    }

    fun delete(key: String) {
        deleteVariable(key)
    }

    fun exists(key: String): Boolean {
        return variableExists(key)
    }

    // Private helper functions

    private fun getVariable(key: String): Storable? {
        val variable: Variable? = db.where("variableKey = ?", key).results(Variable::class.java).firstOrNull()
        return variable?.let { Storable.fromJson(it.variableType, it.variableValue) }
    }

    private fun setVariable(key: String, value: Storable) {
        val variable = Variable(key, value.toJson(), value.javaClass.name)
        db.insert(variable)
    }

    private fun deleteVariable(key: String) {
        db.where("key = ?", key).delete(Variable::class.java)
    }

    private fun variableExists(key: String): Boolean {
        return getVariable(key) != null
    }
}
