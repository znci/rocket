package dev.znci.rocket.scripting.classes

import dev.znci.rocket.scripting.globals.tables.LuaNamespacedKey
import dev.znci.twine.TwineNative
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@Suppress("unused")
class LuaPersistentDataContainer(val pdc: PersistentDataContainer) : TwineNative("") {
    @TwineNativeProperty
    val empty
        get() = pdc.isEmpty

    @TwineNativeFunction
    fun remove(key: LuaNamespacedKey) {
        pdc.remove(key.key)
    }

    @TwineNativeFunction
    fun copyTo(target: LuaPersistentDataContainer, replace: Boolean) {
        pdc.copyTo(target.pdc, replace)
    }

    @TwineNativeFunction
    @TwineOverload
    fun has(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key)
    }

    @TwineNativeFunction
    fun setDouble(key: LuaNamespacedKey, value: Double) {
        pdc.set(key.key, PersistentDataType.DOUBLE, value)
    }

    @TwineNativeFunction
    fun getDouble(key: LuaNamespacedKey): Double? {
        return pdc.get(key.key, PersistentDataType.DOUBLE)
    }

    @TwineNativeFunction
    fun hasDouble(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.BYTE)
    }

    @TwineNativeFunction
    fun setFloat(key: LuaNamespacedKey, value: Float) {
        pdc.set(key.key, PersistentDataType.FLOAT, value)
    }

    @TwineNativeFunction
    fun getFloat(key: LuaNamespacedKey): Float? {
        return pdc.get(key.key, PersistentDataType.FLOAT)
    }

    @TwineNativeFunction
    fun hasFloat(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.FLOAT)
    }

    @TwineNativeFunction
    fun setInt(key: LuaNamespacedKey, value: Int) {
        pdc.set(key.key, PersistentDataType.INTEGER, value)
    }

    @TwineNativeFunction
    fun getInt(key: LuaNamespacedKey): Int? {
        return pdc.get(key.key, PersistentDataType.INTEGER)
    }

    @TwineNativeFunction
    fun hasInt(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.INTEGER)
    }

    @TwineNativeFunction
    fun setLong(key: LuaNamespacedKey, value: Long) {
        pdc.set(key.key, PersistentDataType.LONG, value)
    }

    @TwineNativeFunction
    fun getLong(key: LuaNamespacedKey): Long? {
        return pdc.get(key.key, PersistentDataType.LONG)
    }

    @TwineNativeFunction
    fun hasLong(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LONG)
    }

    @TwineNativeFunction
    fun setShort(key: LuaNamespacedKey, value: Short) {
        pdc.set(key.key, PersistentDataType.SHORT, value)
    }

    @TwineNativeFunction
    fun getShort(key: LuaNamespacedKey): Short? {
        return pdc.get(key.key, PersistentDataType.SHORT)
    }

    @TwineNativeFunction
    fun hasShort(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.SHORT)
    }

    @TwineNativeFunction
    fun setString(key: LuaNamespacedKey, value: String) {
        pdc.set(key.key, PersistentDataType.STRING, value)
    }

    @TwineNativeFunction
    fun getString(key: LuaNamespacedKey): String? {
        return pdc.get(key.key, PersistentDataType.STRING)
    }

    @TwineNativeFunction
    fun hasString(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.STRING)
    }

    @TwineNativeFunction
    fun setBoolean(key: LuaNamespacedKey, value: Boolean) {
        pdc.set(key.key, PersistentDataType.BOOLEAN, value)
    }

    @TwineNativeFunction
    fun getBoolean(key: LuaNamespacedKey): Boolean? {
        return pdc.get(key.key, PersistentDataType.BOOLEAN)
    }

    @TwineNativeFunction
    fun hasBoolean(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.BOOLEAN)
    }

    @TwineNativeFunction
    fun setDoubleList(key: LuaNamespacedKey, value: List<Double>) {
        pdc.set(key.key, PersistentDataType.LIST.doubles(), value)
    }

    @TwineNativeFunction
    fun getDoubleList(key: LuaNamespacedKey): Array<Double>? {
        return pdc.get(key.key, PersistentDataType.LIST.doubles())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasDoubleList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.doubles())
    }

    @TwineNativeFunction
    fun setFloatList(key: LuaNamespacedKey, value: List<Float>) {
        pdc.set(key.key, PersistentDataType.LIST.floats(), value)
    }

    @TwineNativeFunction
    fun getFloatList(key: LuaNamespacedKey): Array<Float>? {
        return pdc.get(key.key, PersistentDataType.LIST.floats())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasFloatList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.floats())
    }

    @TwineNativeFunction
    fun setIntList(key: LuaNamespacedKey, value: List<Int>) {
        pdc.set(key.key, PersistentDataType.LIST.integers(), value)
    }

    @TwineNativeFunction
    fun getIntList(key: LuaNamespacedKey): Array<Int>? {
        return pdc.get(key.key, PersistentDataType.LIST.integers())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasIntList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.integers())
    }

    @TwineNativeFunction
    fun setLongList(key: LuaNamespacedKey, value: List<Long>) {
        pdc.set(key.key, PersistentDataType.LIST.longs(), value)
    }

    @TwineNativeFunction
    fun getLongList(key: LuaNamespacedKey): Array<Long>? {
        return pdc.get(key.key, PersistentDataType.LIST.longs())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasLongList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.longs())
    }

    @TwineNativeFunction
    fun setShortList(key: LuaNamespacedKey, value: List<Short>) {
        pdc.set(key.key, PersistentDataType.LIST.shorts(), value)
    }

    @TwineNativeFunction
    fun getShortList(key: LuaNamespacedKey): Array<Short>? {
        return pdc.get(key.key, PersistentDataType.LIST.shorts())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasShortList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.shorts())
    }

    @TwineNativeFunction
    fun setStringList(key: LuaNamespacedKey, value: List<String>) {
        pdc.set(key.key, PersistentDataType.LIST.strings(), value)
    }

    @TwineNativeFunction
    fun getStringList(key: LuaNamespacedKey): Array<String>? {
        return pdc.get(key.key, PersistentDataType.LIST.strings())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasStringList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.strings())
    }

    @TwineNativeFunction
    fun setBooleanList(key: LuaNamespacedKey, value: List<Boolean>) {
        pdc.set(key.key, PersistentDataType.LIST.booleans(), value)
    }

    @TwineNativeFunction
    fun getBooleanList(key: LuaNamespacedKey): Array<Boolean>? {
        return pdc.get(key.key, PersistentDataType.LIST.booleans())?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasBooleanList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.booleans())
    }

    @TwineNativeFunction
    fun newTagContainer(): LuaPersistentDataContainer {
        return LuaPersistentDataContainer(pdc.adapterContext.newPersistentDataContainer())
    }

    @TwineNativeFunction
    fun setTagContainer(key: LuaNamespacedKey, value: LuaPersistentDataContainer) {
        pdc.set(key.key, PersistentDataType.TAG_CONTAINER, value.pdc)
    }

    @TwineNativeFunction
    fun getTagContainer(key: LuaNamespacedKey): LuaPersistentDataContainer? {
        return pdc.get(key.key, PersistentDataType.TAG_CONTAINER)?.let { LuaPersistentDataContainer(it) }
    }

    @TwineNativeFunction
    fun hasTagContainer(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.TAG_CONTAINER)
    }

    @TwineNativeFunction
    fun setTagContainerList(key: LuaNamespacedKey, value: List<LuaPersistentDataContainer>) {
        pdc.set(key.key, PersistentDataType.LIST.dataContainers(), value.map { it.pdc })
    }

    @TwineNativeFunction
    fun getTagContainerList(key: LuaNamespacedKey): Array<LuaPersistentDataContainer>? {
        return pdc.get(key.key, PersistentDataType.LIST.dataContainers())?.map { LuaPersistentDataContainer(it) }?.toTypedArray()
    }

    @TwineNativeFunction
    fun hasTagContainerList(key: LuaNamespacedKey): Boolean {
        return pdc.has(key.key, PersistentDataType.LIST.dataContainers())
    }
}