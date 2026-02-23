package com.example.fintechsample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fintechsample.ui.NavigationEvent
import com.example.fintechsample.ui.NavigationRouter
import com.example.fintechsample.ui.ReactNativeScreen
import com.example.fintechsample.ui.theme.FintechSampleTheme
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), DefaultHardwareBackBtnHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FintechSampleTheme {
                // Instead of a separate Button + Intent, we use the NavHost directly
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainAppNavigation()
                    }
                }
            }
        }
    }

    override fun invokeDefaultOnBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()

    // Observe the NavigationRouter from the TurboModule
    LaunchedEffect(Unit) {
        NavigationRouter.events.collect { event ->
            when (event) {
                is NavigationEvent.ToOrderBook -> {
                    // Navigate to your OrderBook destination in the Compose graph
                    navController.navigate("order_book/${event.symbol}")
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = "ticker") {
        composable("ticker") {
            // Your existing React Native Ticker Screen wrapper
            ReactNativeScreen(componentName = "CryptoTickerApp")
        }
        composable("order_book/{symbol}") { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: "BTCUSDT"
            // Your React Native Order Book Screen wrapper
            // Pass the symbol as initialProps to the RN component
            ReactNativeScreen(
                componentName = "OrderBook",
                initialProps = bundleOf("symbol" to symbol)
            )
        }
    }
}