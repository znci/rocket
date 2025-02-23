package dev.znci.rocket.scripting.functions

import dev.znci.rocket.scripting.ScriptManager
import dev.znci.rocket.scripting.events.EventListener
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class LuaEvents : LuaTable() {
    init {
        set("on", object : TwoArgFunction() {
            override fun call(eventName: LuaValue, callback: LuaValue): LuaValue {
                val eventClass = EventListener.getEventByName(eventName.tojstring())

                if (eventClass != null) {
                    try {
                        val eventInstance = eventClass.getDeclaredConstructor().newInstance()

                        ScriptManager.usedEvents.put(eventInstance, callback.checkfunction())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                return LuaValue.NIL
            }
        })
    }
}