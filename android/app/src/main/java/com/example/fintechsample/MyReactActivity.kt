package com.example.fintechsample

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyReactActivity : ReactActivity() {
    // This matches the string in index.js
    override fun getMainComponentName(): String = "CryptoTickerApp"

    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return DefaultReactActivityDelegate(
            this,
            mainComponentName,
            fabricEnabled // 0.84 uses Fabric by default
        )
    }
}