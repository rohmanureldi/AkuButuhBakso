package com.eldi.akubutuhbakso.domain.models

import androidx.annotation.DrawableRes
import com.eldi.akubutuhbakso.utils.role.UserRole
import com.google.android.gms.maps.model.LatLng

data class UserData(
    val timestampId: String,
    val name: String,
    val role: UserRole,
    val coord: LatLng,
    @DrawableRes val iconRes: Int,
)
