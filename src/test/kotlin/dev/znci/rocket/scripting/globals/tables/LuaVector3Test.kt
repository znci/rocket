package dev.znci.rocket.scripting.globals.tables

import org.junit.jupiter.api.Test
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.lib.jse.JsePlatform
import kotlin.test.assertEquals

class LuaVector3Test {
    fun run(script: String): Any {
        val globals: Globals = JsePlatform.standardGlobals()
        globals.set("vector3", LuaVectors3().table)

        val newScript = """
            local vector = vector3.new(1, 2, 3)
            local vector2 = vector3.new(4, 5, 6)
            $script
        """.trimIndent()

        val result = globals.load(newScript, "test.lua").call()

        return result
    }

    @Test
    fun `vector3 addition should return the correct value`() {
        val result = run("""
            vector.add(vector3.new(1, 2, 3))
            return vector
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(2.0, table.get("x").todouble())
        assertEquals(4.0, table.get("y").todouble())
        assertEquals(6.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 set should return the correct value`() {
        val result = run("""
            vector.set(vector3.new(5, 6, 7))
            return vector
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(5.0, table.get("x").todouble())
        assertEquals(6.0, table.get("y").todouble())
        assertEquals(7.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 crossProduct should return the correct value`() {
        val result = run("""
            local crossProduct = vector.crossProduct(vector2)
            return crossProduct
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(-3.0, table.get("x").todouble())
        assertEquals(6.0, table.get("y").todouble())
        assertEquals(-3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 cross should return the correct value`() {
        val result = run("""
            local crossProduct = vector.cross(vector2)
            return crossProduct
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(-3.0, table.get("x").todouble())
        assertEquals(6.0, table.get("y").todouble())
        assertEquals(-3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 clone should return the correct value`() {
        val result = run("""
            local clone = vector.clone()
            return clone
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(1.0, table.get("x").todouble())
        assertEquals(2.0, table.get("y").todouble())
        assertEquals(3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 distance should return the correct value`() {
        val result = run("""
            local distance = vector.distance(vector2)
            return distance
        """.trimIndent())

        assertEquals(5.196152, result.toString().toDouble())
    }

    @Test
    fun `vector3 divide should return the correct value`() {
        val result = run("""
            local divided = vector.divide(vector2)
            return divided
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(0.25, table.get("x").todouble())
        assertEquals(0.4, table.get("y").todouble())
        assertEquals(0.5, table.get("z").todouble())
    }

    @Test
    fun `vector3 div should return the correct value`() {
        val result = run("""
            local divided = vector.div(vector2)
            return divided
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(0.25, table.get("x").todouble())
        assertEquals(0.4, table.get("y").todouble())
        assertEquals(0.5, table.get("z").todouble())
    }

    @Test
    fun `vector3 multiply should return the correct value`() {
        val result = run("""
            local multiplied = vector.multiply(vector2)
            return multiplied
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(4.0, table.get("x").todouble())
        assertEquals(10.0, table.get("y").todouble())
        assertEquals(18.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 multiply using non-vector parameters should return the correct value`() {
        val result = run("""
            local multiplied = vector.multiply(4, 5, 6)
            return multiplied
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(4.0, table.get("x").todouble())
        assertEquals(10.0, table.get("y").todouble())
        assertEquals(18.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 mul should return the correct value`() {
        val result = run("""
            local multiplied = vector.mul(vector2)
            return multiplied
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(4.0, table.get("x").todouble())
        assertEquals(10.0, table.get("y").todouble())
        assertEquals(18.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 mul using non-vector parameters should return the correct value`() {
        val result = run("""
            local multiplied = vector.mul(4, 5, 6)
            return multiplied
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(4.0, table.get("x").todouble())
        assertEquals(10.0, table.get("y").todouble())
        assertEquals(18.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 subtract should return the correct value`() {
        val result = run("""
            local subtracted = vector.subtract(vector2)
            return subtracted
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(-3.0, table.get("x").todouble())
        assertEquals(-3.0, table.get("y").todouble())
        assertEquals(-3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 subtract using non-vector parameters should return the correct value`() {
        val result = run("""
            local subtracted = vector.subtract(4, 5, 6)
            return subtracted
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(-3.0, table.get("x").todouble())
        assertEquals(-3.0, table.get("y").todouble())
        assertEquals(-3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 sub should return the correct value`() {
        val result = run("""
            local subtracted = vector.sub(vector2)
            return subtracted
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(-3.0, table.get("x").todouble())
        assertEquals(-3.0, table.get("y").todouble())
        assertEquals(-3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 sub using non-vector parameters should return the correct value`() {
        val result = run("""
            local subtracted = vector.sub(4, 5, 6)
            return subtracted
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(-3.0, table.get("x").todouble())
        assertEquals(-3.0, table.get("y").todouble())
        assertEquals(-3.0, table.get("z").todouble())
    }

    @Test
    fun `vector3 dotProduct should return the correct value`() {
        val result = run("""
            local dotProduct = vector.dotProduct(vector2)
            return dotProduct
        """.trimIndent())

        assertEquals(32.0, result.toString().toDouble())
    }

    @Test
    fun `vector3 dot should return the correct value`() {
        val result = run("""
            local dotProduct = vector.dot(vector2)
            return dotProduct
        """.trimIndent())

        assertEquals(32.0, result.toString().toDouble())
    }

    @Test
    fun `vector3 midpoint should return the correct value`() {
        val result = run("""
            local midpoint = vector.midpoint(vector2)
            return midpoint
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(2.5, table.get("x").todouble())
        assertEquals(3.5, table.get("y").todouble())
        assertEquals(4.5, table.get("z").todouble())
    }

    @Test
    fun `vector3 inAABB should return the correct value`() {
        val result = run("""
            local inAABB = vector.inAABB(vector3.new(0, 0, 0), vector3.new(5, 6, 7))
            return inAABB
        """.trimIndent())

        assertEquals(true, result.toString().toBoolean())
    }

    @Test
    fun `vector3 inAABB with invalid coordinates should return the correct value`() {
        val result = run("""
            local inAABB = vector.inAABB(vector3.new(0, 0, 0), vector3.new(1, 1, 1))
            return inAABB
        """.trimIndent())

        assertEquals(false, result.toString().toBoolean())
    }

    @Test
    fun `vector3 inSphere should return the correct value`() {
        val result = run("""
            local inSphere = vector.inSphere(vector3.new(0, 0, 0), 5)
            return inSphere
        """.trimIndent())

        assertEquals(true, result.toString().toBoolean())
    }

    @Test
    fun `vector3 inSphere with invalid coordinates should return the correct value`() {
        val result = run("""
            local inSphere = vector.inSphere(vector3.new(0, 0, 0), 1)
            return inSphere
        """.trimIndent())

        assertEquals(false, result.toString().toBoolean())
    }

    @Test
    fun `vector3 rotateAround should return the correct value`() {
        val result = run("""
            local rotated = vector.rotateAround(vector3.new(10, 10, 10), 90)
            return rotated
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(2.9642228305135787, table.get("x").todouble())
        assertEquals(0.967701571231181, table.get("y").todouble())
        assertEquals(2.0680755982552386, table.get("z").todouble())
    }

    @Test
    fun `vector3 normalize should return the correct value`() {
        val result = run("""
            local normalized = vector.normalize()
            return normalized
        """.trimIndent())
        val table = result as LuaTable

        assertEquals(0.2672612419124244, table.get("x").todouble())
        assertEquals(0.5345224838248488, table.get("y").todouble())
        assertEquals(0.8017837257372732, table.get("z").todouble())
    }

    @Test
    fun `vector3 magnitude should return the correct value`() {
        val result = run("""
            local magnitude = vector.magnitude
            return magnitude
        """.trimIndent())

        assertEquals(3.7416575, result.toString().toDouble())
    }

    @Test
    fun `vector3 toString should return the correct value`() {
        val result = run("""
            local str = vector.toString()
            return str
        """.trimIndent())

        assertEquals("vector3(1.0, 2.0, 3.0)", result.toString())
    }

    @Test
    fun `vector3 x property should return the correct value`() {
        val result = run("return vector.x")

        assertEquals(1.0, result.toString().toDouble())
    }

    @Test
    fun `vector3 y property should return the correct value`() {
        val result = run("return vector.y")

        assertEquals(2.0, result.toString().toDouble())
    }

    @Test
    fun `vector3 z property should return the correct value`() {
        val result = run("return vector.z")

        assertEquals(3.0, result.toString().toDouble())
    }
}