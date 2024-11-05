package com.eldi.akubutuhbakso.data.mapper

import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
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
        return data.mapNotNull {
            val latLng = runCatching {
                it.coord?.split(",")?.let {
                    val lat = it.first().toDoubleOrNull()
                    val lng = it.last().toDoubleOrNull()

                    if (lat == null || lng == null || it.size != 2) {
                        return@let null
                    }

                    LatLng(lat, lng)
                }
            }.getOrNull()

            if (latLng == null) return@mapNotNull null
            if (it.name.isNullOrBlank() || it.role.isNullOrBlank()) return@mapNotNull null

            val role = if (UserRole.valueOf(it.role) == UserRole.Buyer) "Pembeli" else "Penjual"

            UserData(name = it.name, role = role, coord = latLng)
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
