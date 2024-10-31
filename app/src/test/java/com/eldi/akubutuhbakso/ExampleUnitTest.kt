package com.eldi.akubutuhbakso

import junit.framework.TestCase.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun onReceiveList() {
        val input = """{
    "t": "d",
    "d": {
        "b": {
            "p": "bakso/indonesia/seller",
            "d": {
                "seller1": {
                    "coord": "123,126",
                    "name": "Ikhsan",
                    "role": "Seller"
                },
                "seller2": {
                    "coord": "123,126",
                    "name": "Ikhsan",
                    "role": "Seller2"
                }
            }
        },
        "a": "d"
    }
}"""
        val responseJson = Json.parseToJsonElement(input).jsonObject
        val data = responseJson["d"]?.jsonObject?.get("b")?.jsonObject?.get("d")?.jsonObject
        println()
        println()
        println(parseUser(data!!))
        println()
        println()

        if (parseUser(data) is JsonArray) {
            val dataResult = Json.decodeFromJsonElement<List<UserModel>>(parseUser(data))
            dataResult.forEach {
                println("DATTTAA ->>>>> ${it.name}")
            }
        }

        assertEquals(true, parseUser(data) is JsonArray)
    }

    @Test
    fun onReceive() {
        val input = """{
    "t": "d",
    "d": {
        "b": {
            "p": "bakso/indonesia/seller/seller2",
            "d": {
                "coord": "123,126",
                "name": "Ikhsan",
                "role": "Seller2"
            }
        },
        "a": "d"
    }
}"""
        val responseJson = Json.parseToJsonElement(input).jsonObject
        val data = responseJson["d"]?.jsonObject?.get("b")?.jsonObject?.get("d")?.jsonObject
        println()
        println()
        println(parseUser(data!!))
        println()
        println()
        assertEquals(true, parseUser(data) is JsonObject)
    }

    @Test
    fun emptyResponse() {
        val input = """{"t":"d","d":{"r":0,"b":{"s":"ok","d":{}}}}"""
        val responseJson = Json.parseToJsonElement(input).jsonObject
        val data = responseJson["d"]?.jsonObject?.get("b")?.jsonObject?.get("d")?.jsonObject
        println()
        println()
        println(parseUser(data!!))
        println()
        println()
        val result = parseUser(data)
        assertEquals(true, (result is JsonArray) && result.jsonArray.isEmpty())
    }

    @Test
    fun weirdResponse() {
        val input = """{"t":"c","d":{"t":"h","d":{"ts":1730316127427,"v":"5","h":"s-apse1b-nss-211.asia-southeast1.firebasedatabase.app","s":"mfd3MUXbSftL8TCbX96yJ5VdJziRxgdn"}}}"""
        val responseJson = Json.parseToJsonElement(input).jsonObject
        val data = responseJson["d"]?.jsonObject?.get("b")?.jsonObject?.get("d")?.jsonObject?.let { parseUser(it) }
        println()
        println()
        println(data)
        println()
        println()
        assertEquals(null, data)
    }

    private fun parseUser(dObject: JsonObject): JsonElement {
        return if (dObject.values.all { it is JsonObject }) {
            JsonArray(dObject.values.filterIsInstance<JsonObject>())
        } else {
            dObject
        }
    }

    @Serializable
    data class UserModel(
        val name: String,
        val role: String,
        val coord: String,
    )
}
