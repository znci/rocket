package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.globals.tables.SimpleTest
import dev.znci.rocket.scripting.globals.values.TestValue

object GlobalInitializer {
    fun init(): Boolean {
        ScriptManager.registerGlobal(SimpleTest())
        ScriptManager.registerGlobal(TestValue())

        return true
    }
}