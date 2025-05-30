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
package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.ScriptManager
import dev.znci.rocket.scripting.events.EventListener
import dev.znci.twine.TwineNative
import dev.znci.twine.TwineTable
import dev.znci.twine.annotations.TwineNativeFunction
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class LuaEvents : TwineNative("event") {

    @TwineNativeFunction("on")
    fun registerEvent(eventName: String, callback: (TwineTable) -> Unit) {
        val eventClass = EventListener.getEventByName(eventName)

        if (eventClass != null) {
            if (!EventListener.SUPPORTED_EVENTS.contains(eventClass)) return

            if (ScriptManager.usedEvents[eventClass] == null) EventListener.registerEvent(eventClass)

            // Cache all information about the loaded script
            // Ensures that we can retrieve the relevant information when dealing with unloading

            if (ScriptManager.usedEvents[eventClass] == null) {
                ScriptManager.usedEvents[eventClass] = mutableListOf()
            }
            ScriptManager.usedEvents[eventClass]!!.add(callback)

            ScriptManager.eventScript[callback] = eventClass

            val fileName = getFileNameFromLuaFunction(callback)

            if (ScriptManager.loadedScriptFiles[fileName] == null) {
                ScriptManager.loadedScriptFiles[fileName] = mutableListOf()
            }
            ScriptManager.loadedScriptFiles[fileName]!!.add(callback)
        }
    }

//    init {
//        set("on", object : TwoArgFunction() {
//            override fun call(eventName: LuaValue, callback: LuaValue): LuaValue {
//                val eventClass = EventListener.getEventByName(eventName.tojstring())
//
//                if (eventClass != null) {
//                    if (!EventListener.SUPPORTED_EVENTS.contains(eventClass)) return LuaValue.NIL.also { println("Not contained") }
//
//                    if (ScriptManager.usedEvents[eventClass] == null) EventListener.registerEvent(eventClass)
//
//                    // Cache all information about the loaded script
//                    // Ensures that we can retrieve the relevant information when dealing with unloading
//
//                    if (ScriptManager.usedEvents[eventClass] == null) {
//                        ScriptManager.usedEvents[eventClass] = mutableListOf()
//                    }
//                    val function = callback.checkfunction()
//                    ScriptManager.usedEvents[eventClass]!!.add(function)
//
//                    ScriptManager.eventScript[function] = eventClass
//
//                    val fileName = getFileNameFromLuaFunction(function)
//
//                    if (ScriptManager.loadedScriptFiles[fileName] == null) {
//                        ScriptManager.loadedScriptFiles[fileName] = mutableListOf()
//                    }
//                    ScriptManager.loadedScriptFiles[fileName]!!.add(function)
//                }
//
//                return NIL
//            }
//        })
//        }

    /**
     * Scuffed stuff, let's find a better way if we can
     * @see ScriptManager.runScript
     */

    private fun getFileNameFromLuaFunction(function: Function1<TwineTable, Unit>?): String {
        var fileName = ""
        var previousChar: Char? = null
        var record = false
        for (char in function.toString()) {
            if ((previousChar ?: ' ') == ':' && char == ':') if (!record) record = true else break
            if (record) fileName = "$fileName$char"
            previousChar = char
        }
        return fileName.subSequence(1, fileName.length-1).toString()
    }
}