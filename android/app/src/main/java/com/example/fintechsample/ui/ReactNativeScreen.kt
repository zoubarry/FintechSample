package com.example.fintechsample.ui

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.facebook.react.ReactApplication
import com.facebook.react.ReactDelegate

@Composable
fun ReactNativeScreen(
    componentName: String,
    initialProps: Bundle? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ReactDelegate is the modern wrapper that handles ReactHost and Fabric internally
    val reactDelegate = remember {
        val reactApplication = context.applicationContext as ReactApplication
        ReactDelegate(
            context as Activity,
            reactApplication.reactHost, // Uses the Bridgeless ReactHost
            componentName,
            initialProps
        ).apply {
            // THIS IS THE MISSING LINE!
            // It forces the delegate to build the ReactRootView and start the JS bundle.
            loadApp()
        }
    }

    // Sync the React Native engine lifecycle with the Android Compose lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> reactDelegate.onHostResume()
                Lifecycle.Event.ON_PAUSE -> reactDelegate.onHostPause()
                Lifecycle.Event.ON_DESTROY -> reactDelegate.onHostDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            reactDelegate.onHostDestroy()
        }
    }

    // Render the view that the Delegate created for us
    AndroidView(
        factory = {
            reactDelegate.reactRootView ?: throw IllegalStateException("ReactRootView is null")
        },
        modifier = Modifier.fillMaxSize()
    )
}