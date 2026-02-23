package com.example.fintechsample.ui.ticker// TickerPackage.kt
import com.example.fintechsample.ui.NavigationModule
import com.example.fintechsample.ui.orderbook.OrderBookModule
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class TickerPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
            TickerModule(reactContext),      // Your legacy bridge
            OrderBookModule(reactContext),   // Your new turbo module
            NavigationModule(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}