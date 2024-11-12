package com.eldi.akubutuhbakso.presentation.navigation

import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

@Serializable
data class MapDestination(
    val userName: String,
    val userRole: UserRole,
    val timeStampIdentifier: String = System.currentTimeMillis().toString(),
)
