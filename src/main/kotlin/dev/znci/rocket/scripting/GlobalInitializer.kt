package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.globals.SimpleTest
import dev.znci.rocket.scripting.globals.Test

object GlobalInitializer {
    fun init(): Boolean {
        ScriptManager.registerGlobal(Test())
        ScriptManager.registerGlobal(SimpleTest())

        return true
    }
}