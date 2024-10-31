package com.eldi.akubutuhbakso.utils.role

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole(val value: String) {
    Seller("Penjual"),
    Buyer("Pembeli"),
    ;

    companion object {
        fun getRoleByStr(strRole: String): UserRole {
            return if (strRole.equals("Penjual", true)) Seller else Buyer
        }
    }
}
