package com.eldi.akubutuhbakso.service

import android.util.Log
import com.eldi.akubutuhbakso.data.model.FirebaseWsListenerRequest
import com.eldi.akubutuhbakso.utils.defaultJson
import com.eldi.akubutuhbakso.utils.role.UserRole
import kotlinx.serialization.encodeToString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class WsClient(
    private val socketUrl: String,
) {
    private lateinit var webSocket: okhttp3.WebSocket
    private var wsListener: LocationShareSocketListener? = null
    private var shouldReconnect = false

    private fun initWebSocket() {
        val request = Request.Builder().url(url = socketUrl).build()
        if (wsListener == null) {
            throw NullPointerException("Socket listener is null")
        }

        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        webSocket = client.newWebSocket(request, webSocketListener)

        client.dispatcher.executorService.shutdown()
    }

    fun connect(listener: LocationShareSocketListener) {
        Log.e("socketCheck", "connect()")
        shouldReconnect = false
        wsListener = listener
        initWebSocket()
    }

    fun reconnect() {
        Log.e("socketCheck", "reconnect()")
        initWebSocket()
    }

    fun requestListen(role: UserRole) {
        val request = FirebaseWsListenerRequest(role.name.lowercase())

        val requestListenerJson = defaultJson.encodeToString<FirebaseWsListenerRequest>(request)
        sendMessage(requestListenerJson)
    }

    // send
    fun sendMessage(message: String) {
        Log.e("socketCheck", "sendMessage($message)")
        if (::webSocket.isInitialized) webSocket.send(message)
    }

    fun disconnect() {
        if (::webSocket.isInitialized) webSocket.close(1000, "Do not need connection anymore.")
        shouldReconnect = false
    }

    interface LocationShareSocketListener {
        fun onOpen() {}
        fun onMessage(message: String) {}
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
            wsListener?.onOpen()
            Log.e("socketCheck", "onOpen()")
        }

        override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
            Log.e("socketCheck", "onMessage() -> $text")
            wsListener?.onMessage(text)
        }

        override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            Log.e("socketCheck", "onClosing()")
        }

        override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            Log.e("socketCheck", "onClosed()")
            if (shouldReconnect) reconnect()
        }

        override fun onFailure(
            webSocket: okhttp3.WebSocket,
            t: Throwable,
            response: Response?,
        ) {
            Log.e("socketCheck", "onFailure()", t)
            if (shouldReconnect) reconnect()
        }
    }
}
