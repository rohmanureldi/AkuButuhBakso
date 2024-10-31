package com.eldi.akubutuhbakso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    val name: String?,
    val role: String?,
    val coord: String?,
)
