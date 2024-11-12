package com.eldi.akubutuhbakso.data.model

import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    val timestampId: String?,
    val name: String?,
    val role: String?,
    val coord: String?,
) {
    val classifiedRole: UserRole get() {
        return UserRole.valueOf(role.orEmpty())
    }
}

@Serializable
sealed class UserDataResponseType {

    @Serializable
    data class ListOfUsers(val data: List<UserDataResponse>) : UserDataResponseType()

    @Serializable
    data class UserDeleted(val userTimestamp: String) : UserDataResponseType()

    @Serializable
    data class UserAdded(val data: UserDataResponse) : UserDataResponseType()
}
