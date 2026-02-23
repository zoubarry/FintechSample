package com.example.fintechsample.ui.ticker

import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*

class TickerRepository(private val client: OkHttpClient) {
    fun getTickerFlow(symbol: String): Flow<String> = callbackFlow {
        val lowerSymbol = symbol.lowercase()
        val url = "wss://stream.binance.us:9443/ws/$lowerSymbol@ticker"

        Log.d("TickerDebug", "Attempting connection to: $url")

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("TickerDebug", "WebSocket Connection OPEN for $lowerSymbol")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // Log a snippet to confirm data is actually arriving
                Log.d("TickerDebug", "Message Received: ${text.take(30)}...")
                trySend(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("TickerDebug", "CONNECTION FAILURE: ${t.message}")
                t.printStackTrace()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("TickerDebug", "WebSocket Closing: $reason")
            }
        }

        val request = Request.Builder().url(url).build()
        val ws = client.newWebSocket(request, listener)

        awaitClose {
            Log.d("TickerDebug", "Flow closed, canceling WebSocket")
            ws.close(1000, "User changed symbol or screen left")
        }
    }
}