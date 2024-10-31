package com.eldi.akubutuhbakso.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirebaseWsListenerRequest(
    @Serializable @SerialName("t") val messageDataType: String = "d",
    @Serializable @SerialName("d") val data: FirebaseDataRequest = FirebaseDataRequest(),
) {
    constructor(roleToListen: String) : this(
        data = FirebaseDataRequest(
            body = FirebaseBodyRequest(
                "/bakso/indonesia/$roleToListen",
            ),
        ),
    )
}

@Serializable
data class FirebaseDataRequest(
    @Serializable @SerialName("a") val actionType: String = "q", // q = listen
    @Serializable @SerialName("b") val body: FirebaseBodyRequest = FirebaseBodyRequest(),
)

@Serializable
data class FirebaseBodyRequest(
    @Serializable @SerialName("p") val pathToListen: String = "/bakso/indonesia",
)
