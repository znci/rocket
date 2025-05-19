package dev.znci.rocket.scripting.globals.tables

import dev.znci.rocket.scripting.annotations.Global
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import org.bukkit.NamespacedKey

@Global
@Suppress("unused")
class LuaNamespacedKeys : TwineNative("namespacedKey") {
    @TwineNativeFunction
    fun new(namespace: String, key: String): LuaNamespacedKey {
        return LuaNamespacedKey(NamespacedKey(namespace, key))
    }
}

@Suppress("unused")
class LuaNamespacedKey(val key: NamespacedKey) : TwineNative("") {
    @TwineNativeProperty
    val namespace: String
        get() = key.namespace

    @TwineNativeProperty("key")
    val keyValue: String
        get() = key.key
}