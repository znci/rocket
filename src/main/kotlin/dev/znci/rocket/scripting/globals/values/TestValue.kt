package dev.znci.rocket.scripting.globals.values

import dev.znci.twine.TwineProperty

class TestValue : TwineProperty("testValue") {
    init {
        this.value = "test"
    }
}