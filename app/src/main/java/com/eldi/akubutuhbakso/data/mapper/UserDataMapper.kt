package com.eldi.akubutuhbakso.data.mapper

import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.domain.models.UserData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

object UserDataMapper {
    fun parseFromWsMessage(response: String): List<UserDataResponse> {
        val responseJson = Json.parseToJsonElement(response).jsonObject
        val data = responseJson["d"]?.jsonObject?.get("b")?.jsonObject?.get("d")?.jsonObject?.let {
            val parsed = parseUser(it)
            Json.decodeFromJsonElement<List<UserDataResponse>>(parsed)
        }
        return data.orEmpty()
    }

    fun parseToUserData(data: List<UserDataResponse>): List<UserData> {
        return data.map {
            UserData(name = it.name, role = it.role, coord = it.coord)
        }
    }

    private fun parseUser(dObject: JsonObject): JsonElement {
        return if (dObject.values.all { it is JsonObject }) {
            JsonArray(dObject.values.filterIsInstance<JsonObject>())
        } else {
            JsonArray(listOf(dObject))
        }
    }
}
