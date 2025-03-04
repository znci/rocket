package dev.znci.rocket.scripting.functions

import dev.znci.rocket.scripting.PlayerManager
import org.bukkit.Bukkit
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class LuaHTTPClient : LuaTable() {
    init {
        set("get", object : OneArgFunction() {
            override fun call(url: LuaValue): LuaValue {
                return try {
                    val response = httpGet(url.tojstring())

                    LuaValue.valueOf(response)
                } catch (e: Exception) {
                    LuaValue.NIL
                }
            }
        })
    }

    private fun httpGet(url: String): String {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }
}