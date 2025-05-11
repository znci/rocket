package dev.znci.rocket.scripting.globals.tables

import org.junit.jupiter.api.Test
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.JsePlatform
import kotlin.test.assertEquals

class LuaMathTest {
    fun run(script: String): Any {
        val globals: Globals = JsePlatform.standardGlobals()
        globals.set("vector3", LuaVectors3().table)
        globals.set("math", LuaMath().table)

        val result = globals.load(script, "test.lua").call()

        return result
    }

    @Test
    fun `acos should return the correct value`() {
        val result = run("return math.acos(0.5)")

        assertEquals(1.0471976, result.toString().toDouble())
    }

    @Test
    fun `asin should return the correct value`() {
        val result = run("return math.asin(0.5)")

        assertEquals(0.5235988, result.toString().toDouble())
    }

    @Test
    fun `atan should return the correct value`() {
        val result = run("return math.atan(1)")

        assertEquals(0.7853982, result.toString().toDouble())
    }

    @Test
    fun `atan2 should return the correct value`() {
        val result = run("return math.atan2(1, 1)")

        assertEquals(0.7853982, result.toString().toDouble())
    }

    @Test
    fun `random should return a value between 0 and 1`() {
        val result = run("return math.random()")

        assert(result.toString().toDouble() in 0.0..1.0)
    }

    @Test
    fun `random(1, 100) should return a value between 1 and 100`() {
        val result = run("return math.random(1, 100)")

        assert(result.toString().toDouble() in 1.0..100.0)
    }

    @Test
    fun `randomInt should return a value between 1 and 100`() {
        val result = run("return math.randomInt(1, 100)")

        assert(result.toString().toDouble() in 1.0..100.0)
    }

    @Test
    fun `randomInt should return a value between 0 and 1`() {
        val result = run("return math.randomInt()")

        assert(result.toString().toDouble() in 0.0..1.0)
    }

    @Test
    fun `cos should return the correct value`() {
        val result = run("return math.cos(0)")

        assertEquals(1.0, result.toString().toDouble())
    }

    @Test
    fun `sin should return the correct value`() {
        val result = run("return math.sin(0)")

        assertEquals(0.0, result.toString().toDouble())
    }

    @Test
    fun `tan should return the correct value`() {
        val result = run("return math.tan(0)")

        assertEquals(0.0, result.toString().toDouble())
    }

    @Test
    fun `min should return the correct value`() {
        val result = run("return math.min(1, 2)")

        assertEquals(1.0, result.toString().toDouble())
    }

    @Test
    fun `min with a vector should return the correct value`() {
        val result = run("return math.min(vector3.new(1, 2, 3))")

        assertEquals(1.0, result.toString().toDouble())
    }

    @Test
    fun `max should return the correct value`() {
        val result = run("return math.max(1, 2)")

        assertEquals(2.0, result.toString().toDouble())
    }

    @Test
    fun `max with a vector should return the correct value`() {
        val result = run("return math.max(vector3.new(1, 2, 3))")

        assertEquals(3.0, result.toString().toDouble())
    }

    @Test
    fun `abs should return the correct value`() {
        val result = run("return math.abs(-1)")

        assertEquals(1.0, result.toString().toDouble())
    }

    @Test
    fun `abs with a vector should return the correct value`() {
        val result = run("return math.abs(vector3.new(-1, -2, -3))")
        val table = result as LuaTable

        assertEquals(1.0, table.get("x").todouble())
        assertEquals(2.0, table.get("y").todouble())
        assertEquals(3.0, table.get("z").todouble())
    }
}