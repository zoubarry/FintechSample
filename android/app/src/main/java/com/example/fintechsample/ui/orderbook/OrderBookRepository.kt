package com.example.fintechsample.ui.orderbook

import org.json.JSONArray
import org.json.JSONObject
import java.util.TreeMap
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

@Singleton
class OrderBookRepository @Inject constructor() {
    // Bids: Sorted High -> Low (Price as Double for sorting, Quantity as Double)
    private val bids = TreeMap<Double, Double>(reverseOrder())
    // Asks: Sorted Low -> High
    private val asks = TreeMap<Double, Double>()

    /**
     * Public entry point: Accepts the raw JSON from the WebSocket.
     * Logic is synchronized to handle simultaneous WS writes and JSI reads.
     */
    fun updateBook(json: JSONObject) {
        synchronized(this) {
            try {
                // Clear and rebuild for Partial Depth streams
                bids.clear()
                asks.clear()

                // Hide the parsing logic in private helpers to keep this clean
                parseLevels(json.optJSONArray("bids"), bids)
                parseLevels(json.optJSONArray("asks"), asks)

                Log.v("OrderBookRepo", "Updated: ${bids.size} bids, ${asks.size} asks")
            } catch (e: Exception) {
                Log.e("OrderBookRepo", "Failed to update book: ${e.message}")
            }
        }
    }

    /**
     * JSI Hook: Returns the top of the book as a valid JSON string for the RN UI.
     */
    fun getSerializedBook(): String {
        synchronized(this) {
            val bidsList = bids.entries.take(10).map { "[${it.key},${it.value}]" }
            val asksList = asks.entries.take(10).map { "[${it.key},${it.value}]" }

            return """{"bids":[${bidsList.joinToString(",")}], "asks":[${asksList.joinToString(",")}]}"""
        }
    }

    /**
     * Private helper to keep the public updateBook() method readable.
     */
    private fun parseLevels(array: JSONArray?, map: MutableMap<Double, Double>) {
        array?.let {
            for (i in 0 until it.length()) {
                val level = it.getJSONArray(i)
                val price = level.getString(0).toDouble()
                val quantity = level.getString(1).toDouble()
                map[price] = quantity
            }
        }
    }

    fun getBookAsMap(): WritableMap {
        val map = Arguments.createMap()
        val bidsArray = Arguments.createArray()
        val asksArray = Arguments.createArray()

        synchronized(this) {
            // Build Bids Array: [[price, qty], [price, qty]...]
            bids.entries.take(10).forEach { entry ->
                val level = Arguments.createArray()
                level.pushDouble(entry.key)
                level.pushDouble(entry.value)
                bidsArray.pushArray(level)
            }

            // Build Asks Array
            asks.entries.take(10).forEach { entry ->
                val level = Arguments.createArray()
                level.pushDouble(entry.key)
                level.pushDouble(entry.value)
                asksArray.pushArray(level)
            }
        }

        map.putArray("bids", bidsArray)
        map.putArray("asks", asksArray)
        return map
    }
}