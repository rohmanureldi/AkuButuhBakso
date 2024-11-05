package com.eldi.akubutuhbakso.domain.models

import com.google.android.gms.maps.model.LatLng

data class UserData(
    val name: String,
    val role: String,
    val coord: LatLng,
)
