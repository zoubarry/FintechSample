package com.example.fintechsample.ui

import com.example.fintechsample.specs.NativeNavigationSpec
import com.facebook.react.bridge.ReactApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NavigationModule(context: ReactApplicationContext) : NativeNavigationSpec(context) {
    
    override fun getName() = "NavigationModule"

    override fun navigateToOrderBook(symbol: String) {
        // Use a global scope or a singleton manager to emit the event
        // because the TurboModule lifecycle is independent of the Activity
        MainScope().launch {
            NavigationRouter.emit(NavigationEvent.ToOrderBook(symbol))
        }
    }
}

/**
 * A simple Singleton Router to bridge the gap between 
 * the TurboModule (Native Layer) and the Compose UI Layer.
 */
object NavigationRouter {
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun emit(event: NavigationEvent) {
        _events.tryEmit(event)
    }
}

sealed class NavigationEvent {
    data class ToOrderBook(val symbol: String) : NavigationEvent()
}