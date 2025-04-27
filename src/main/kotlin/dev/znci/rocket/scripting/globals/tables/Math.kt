package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineOverload
import java.lang.Math
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
    @TwineOverload
    fun randomInt(min: Double, max: Double): Int {
        if (min >= max) {
            throw RocketError("min value should be less than max value")
        }

        return (min + (max - min) * Math.random()).roundToInt()
    }

    @TwineNativeFunction
    @TwineOverload
    fun randomInt(): Int {
        return randomInt(0.0, 1.0)
    }

    @TwineNativeFunction
    fun cos(x: Double): Double {
        return kotlin.math.cos(x)
    }

    @TwineNativeFunction
    fun sin(x: Double): Double {
        return kotlin.math.sin(x)
    }

    @TwineNativeFunction
    fun tan(x: Double): Double {
        return kotlin.math.tan(x)
    }

    @TwineNativeFunction
    fun acos(x: Double): Double {
        return kotlin.math.acos(x)
    }

    @TwineNativeFunction
    fun asin(x: Double): Double {
        return kotlin.math.asin(x)
    }

    @TwineNativeFunction
    fun atan(x: Double): Double {
        return kotlin.math.atan(x)
    }

    @TwineNativeFunction
    fun atan2(y: Double, x: Double): Double {
        return kotlin.math.atan2(y, x)
    }

    @TwineNativeFunction
    fun sqrt(x: Double): Double {
        return kotlin.math.sqrt(x)
    }

    @TwineNativeFunction
    fun ceil(x: Double): Double {
        return kotlin.math.ceil(x)
    }

    @TwineNativeFunction
    @TwineOverload
    fun floor(x: Double): Double {
        return kotlin.math.floor(x)
    }

    @TwineNativeFunction
    @TwineOverload
    fun floor(vector3: LuaVector3): LuaVector3 {
        return LuaVector3(
            kotlin.math.floor(vector3.xProperty),
            kotlin.math.floor(vector3.yProperty),
            kotlin.math.floor(vector3.zProperty)
        )
    }

    @TwineNativeFunction
    @TwineOverload
    fun abs(x: Double): Double {
        return kotlin.math.abs(x)
    }

    @TwineNativeFunction
    @TwineOverload
    fun abs(vector3: LuaVector3): LuaVector3 {
        return LuaVector3(
            kotlin.math.abs(vector3.xProperty),
            kotlin.math.abs(vector3.yProperty),
            kotlin.math.abs(vector3.zProperty)
        )
    }

//    @TwineNativeFunction
//    @TwineOverload
//    fun max(vararg values: Double): Double {
//        return values.maxOrNull() ?: 0.0
//    }

    // TODO: Add overload for max with vararg
    // TODO: Fix max and min
    @TwineNativeFunction
    @TwineOverload
    fun max(vector3: LuaVector3): Double {
        return kotlin.math.max(
            kotlin.math.max(vector3.xProperty, vector3.yProperty),
            vector3.zProperty
        )
    }

//    @TwineNativeFunction
//    @TwineOverload
//    fun min(vararg values: Double): Double {
//        return values.minOrNull() ?: 0.0
//    }

    @TwineNativeFunction
    @TwineOverload
    fun min(vector3: LuaVector3): Double {
        return kotlin.math.min(
            kotlin.math.min(vector3.xProperty, vector3.yProperty),
            vector3.zProperty
        )
    }

    @TwineNativeFunction
    @TwineOverload
    fun round(x: Double): Double {
        return kotlin.math.round(x)
    }

    @TwineNativeFunction
    @TwineOverload
    fun round(vector3: LuaVector3): LuaVector3 {
        return LuaVector3(
            kotlin.math.round(vector3.xProperty),
            kotlin.math.round(vector3.yProperty),
            kotlin.math.round(vector3.zProperty)
        )
    }

    @TwineNativeFunction
    fun toDegrees(radians: Double): Double {
        return Math.toDegrees(radians)
    }
}