/**
 * Copyright 2025 znci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.znci.rocket.scripting.globals.tables

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import dev.znci.rocket.scripting.annotations.Global
import dev.znci.rocket.scripting.api.RocketError
import dev.znci.twine.TwineNative
import dev.znci.twine.TwineTable
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpConnectTimeoutException
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

// TODO: Add map support, and come back to HTTPClient later.

data class HTTPOptions(
    val timeout: Int? = 30000,
    val followRedirects: Boolean? = true,
//    val headers: Map<String, String>?,
//    val body: Map<String, String>?
) : TwineTable("")

@Global
class LuaHTTPClient : TwineNative("http") {
    @TwineNativeFunction("get")
    fun sendGet(url: String, options: HTTPOptions): HTTPResponse {
        return try {
            val client = getClient(options)

            val requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()

//            applyHeaders(requestBuilder, options)

            val request = requestBuilder.build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val responseBody = response.body()
            val jsonElement = JsonParser.parseString(responseBody)

            HTTPResponse(
                textContent = responseBody.toString(),
                jsonContent = jsonElement
            )
        } catch (e: HttpConnectTimeoutException) {
            throw RocketError(getTimeoutMessage(options.timeout))
        }
    }

    private fun getClient(options: HTTPOptions): HttpClient {
        return HttpClient.newBuilder().apply {
            options.timeout?.let {
                connectTimeout(Duration.ofMillis(it.toLong()))
            }
            followRedirects(
                if (options.followRedirects == true)
                    HttpClient.Redirect.NORMAL
                else
                    HttpClient.Redirect.NEVER
            )
        }.build()
    }

//    fun applyHeaders(requestBuilder: HttpRequest.Builder, options: HTTPOptions) {
//        options.headers?.forEach { (key, value) ->
//            requestBuilder.header(key, value.toString())
//        }
//    }

    private fun getTimeoutMessage(timeout: Int?): String {
        return "HTTP Client request timed out after ${timeout}ms. Maybe your timeout threshold is too low."
    }
}

class HTTPResponse(
    val textContent: String,
    val jsonContent: JsonElement?
) : TwineNative("") {
    @TwineNativeProperty
    val text: String
        get() {
            return textContent
        }

    @TwineNativeProperty
    val json: TwineTable?
        get() {
            if (jsonContent == null) {
                return null
            }
            return fromJSON(jsonContent)
        }
}
