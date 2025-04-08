package dev.znci.rocket.scripting.globals.values

import dev.znci.rocket.scripting.api.RocketProperty

class TestValue : RocketProperty("testValue") {
    init {
        this.value = "test"
    }
}