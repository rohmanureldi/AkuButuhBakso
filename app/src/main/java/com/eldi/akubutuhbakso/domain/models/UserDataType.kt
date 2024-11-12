package com.eldi.akubutuhbakso.domain.models

sealed class UserDataType {

    data class ListOfUsers(val data: List<UserData>) : UserDataType()

    data class UserDeleted(val userTimestamp: String) : UserDataType()

    data class UserAdded(val data: UserData) : UserDataType()
}
