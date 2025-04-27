package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineOverload
import java.lang.Math
import kotlin.math.round
import kotlin.math.roundToInt

@Global
class Math : TwineNative("math") {
    @TwineNativeFunction
    @TwineOverload
    fun random(min: Double, max: Double): Double {
        if (min >= max) {
            throw RocketError("min value should be less than max value")
        }

        return (min + (max - min) * Math.random())
    }

    @TwineNativeFunction
    @TwineOverload
    fun random(): Double {
        return random(0.0, 1.0)
    }

    @TwineNativeFunction
    fun randomInt(min: Int, max: Int): Double {
        if (min >= max) {
            throw RocketError("min value should be less than max value")
        }

        return (min + (max - min) * Math.random()).roundToInt().toDouble()
    }
}