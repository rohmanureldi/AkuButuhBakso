package com.eldi.akubutuhbakso.utils

import kotlinx.serialization.json.Json

val defaultJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}
