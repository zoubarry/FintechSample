package com.example.fintechsample.ui.ticker

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import kotlinx.coroutines.launch

// TickerModule.kt
class TickerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "TickerModule"

    @ReactMethod
    fun startTicker(symbol: String) {
        val activity = reactApplicationContext.currentActivity as? ComponentActivity ?: return

        // With Hilt, you just use the standard ViewModelProvider.
        // Hilt's Activity-level injection handles the Factory behind the scenes.
        val viewModel = ViewModelProvider(activity)[TickerViewModel::class.java]
        viewModel.setSymbol(symbol)

        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tickerData.collect { json ->
                    Log.d("TickerDebug", "Received from VM for $symbol: $json") // Add this!
                    json?.let { emitEvent("onTickerUpdate", it) }
                }
            }
        }
    }

    private fun emitEvent(eventName: String, params: String) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }
}
