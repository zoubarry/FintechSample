package com.example.fintechsample.ui.orderbook

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderBookWebSocketClient @Inject constructor(
    private val client: OkHttpClient,
    private val repository: OrderBookRepository
) : WebSocketListener() {

    private var webSocket: WebSocket? = null
    private val gson = Gson()

    fun connect(symbol: String) {
        val request = Request.Builder()
            .url("wss://stream.binance.us:9443/ws/${symbol.lowercase()}@depth20@100ms")
            .build()
        webSocket = client.newWebSocket(request, this)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        // LOG 1: Raw data check
        // Log only the first 100 chars to avoid flooding Logcat
        Log.d("OrderBookWS", "Raw Message Received: ${text.take(100)}...")

        try {
            val json = JSONObject(text)
            // Check if the message contains the expected "bids" and "asks"
            if (json.has("bids")) {
                repository.updateBook(json)
                // LOG 2: Successful parse
                Log.v("OrderBookWS", "Successfully sent to Repository")
            } else {
                Log.w("OrderBookWS", "Message received but missing 'bids' key")
            }
        } catch (e: Exception) {
            Log.e("OrderBookWS", "JSON Parse Error: ${e.message}")
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User stopped")
    }
}

// Data Classes for GSON
data class DepthUpdateResponse(
    val lastUpdateId: Long,
    val bids: List<List<String>>, // [["price", "qty"], ...]
    val asks: List<List<String>>
)