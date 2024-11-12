package com.eldi.akubutuhbakso.data.mapper

import android.util.Log
import com.eldi.akubutuhbakso.R
import com.eldi.akubutuhbakso.data.model.UserDataResponse
import com.eldi.akubutuhbakso.data.model.UserDataResponseType
import com.eldi.akubutuhbakso.domain.models.UserData
import com.eldi.akubutuhbakso.domain.models.UserDataType
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object UserDataMapper {
    fun parseFromWsMessage(response: String): UserDataResponseType? {
        val responseJson = Json.parseToJsonElement(response).jsonObject
        val data =
            runCatching {
                responseJson["d"]?.jsonObject?.get("b")?.let {
                    val isHavePath = it.jsonObject.containsKey("p")
                    val roleInPath = if (isHavePath) {
                        it.jsonObject["p"]
                            ?.jsonPrimitive
                            ?.content
                            ?.split("/")
                            ?.lastOrNull()
                    } else {
                        null
                    }

                    val data = it.jsonObject["d"]
                    Log.e("Mapper", "parseFromWsMessage: havePath -> $isHavePath")
                    Log.e("Mapper", "parseFromWsMessage: roleInPath -> $roleInPath")
                    when {
                        !isHavePath -> null
                        roleInPath?.equals("seller", true) == true || roleInPath?.equals(
                            "buyer",
                            true,
                        ) == true -> {
                            val parsed = parseUser(data?.jsonObject ?: JsonObject(mapOf()))
                            val users = Json.decodeFromJsonElement<List<UserDataResponse>>(parsed)
                            Log.e("Mapper", "parseFromWsMessage: first -> $users")
                            UserDataResponseType.ListOfUsers(users)
                        }

                        roleInPath != null && data is JsonNull -> {
                            val timestampId = roleInPath
                                .split("-")
                                .lastOrNull()
                            Log.e("Mapper", "parseFromWsMessage: second -> $timestampId")

                            timestampId?.let { UserDataResponseType.UserDeleted(it) }
                        }

                        roleInPath != null && data is JsonObject -> {
                            val parsed = parseUser(data.jsonObject)
                            val users = Json.decodeFromJsonElement<List<UserDataResponse>>(parsed)
                            Log.e("Mapper", "parseFromWsMessage: third -> $users")

                            users.firstOrNull()?.let(UserDataResponseType::UserAdded)
                        }

                        else -> {
                            Log.e("Mapper", "parseFromWsMessage: else")

                            null
                        }
                    }
                }
            }.getOrElse {
                Log.e("Mapper", "parseFromWsMessage: error", it)

                null
            }

        return data
    }

    fun parseToUserData(response: UserDataResponseType): UserDataType {
        return when (response) {
            is UserDataResponseType.ListOfUsers -> {
                val result = response.data.mapNotNull {
                    val latLng = parseCoordFromString(it.coord) ?: return@mapNotNull null

                    if (it.name.isNullOrBlank() || it.role.isNullOrBlank() || it.timestampId.isNullOrBlank()) return@mapNotNull null

                    UserData(
                        timestampId = it.timestampId,
                        name = it.name,
                        role = it.classifiedRole,
                        coord = latLng,
                        iconRes = if (it.classifiedRole == UserRole.Buyer) R.drawable.ic_buyer else R.drawable.ic_seller,
                    )
                }

                UserDataType.ListOfUsers(result)
            }

            is UserDataResponseType.UserAdded -> {
                val latLng = parseCoordFromString(response.data.coord) ?: LatLng(0.0, 0.0)
                val result = UserData(
                    timestampId = response.data.timestampId.orEmpty(),
                    name = response.data.name.orEmpty(),
                    role = response.data.classifiedRole,
                    coord = latLng,
                    iconRes = if (response.data.classifiedRole == UserRole.Buyer) R.drawable.ic_buyer else R.drawable.ic_seller,
                )
                UserDataType.UserAdded(result)
            }

            is UserDataResponseType.UserDeleted -> {
                UserDataType.UserDeleted(response.userTimestamp)
            }
        }
    }
}

private fun parseUser(dObject: JsonObject): JsonElement {
    return if (dObject.values.all { it is JsonObject }) {
        JsonArray(dObject.values.filterIsInstance<JsonObject>())
    } else {
        JsonArray(listOf(dObject))
    }
}

private fun parseCoordFromString(coordStr: String?): LatLng? {
    return runCatching {
        coordStr?.split(",")?.let {
            val lat = it.first().toDoubleOrNull()
            val lng = it.last().toDoubleOrNull()

            if (lat == null || lng == null || it.size != 2) {
                return@let null
            }

            LatLng(lat, lng)
        }
    }.getOrNull()
}
