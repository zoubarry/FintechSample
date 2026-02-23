package com.example.fintechsample.ui.orderbook

import com.example.fintechsample.specs.NativeOrderBookSpec
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import dagger.hilt.android.EntryPointAccessors

class OrderBookModule(context: ReactApplicationContext) : NativeOrderBookSpec(context) {

    // Hilt Injection for TurboModules (since they aren't Activity-bound)
    private val repository: OrderBookRepository by lazy {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            OrderBookEntryPoint::class.java
        )
        entryPoint.orderBookRepository()
    }

    override fun getName() = "OrderBookModule"

    override fun startOrderBook(symbol: String) {
        // Obtain via Hilt EntryPoint as we did before
        val entryPoint = EntryPointAccessors.fromApplication(
            reactApplicationContext.applicationContext,
            OrderBookEntryPoint::class.java
        )
        val wsClient = entryPoint.webSocketClient()
        wsClient.connect(symbol)
    }

    override fun stopOrderBook() { /* Close WS */ }

    // This is called Synchronously from JS via JSI
    override fun getBestBidSync(): String? {
        return repository.getSerializedBook() // Repository might return null if book is empty
    }

    override fun getOrderBookSnapshot(): WritableMap? {
        return repository.getBookAsMap()
    }
}