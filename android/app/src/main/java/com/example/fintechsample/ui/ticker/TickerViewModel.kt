package com.example.fintechsample.ui.ticker// TickerViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TickerViewModel @Inject constructor(
    private val repository: TickerRepository
) : ViewModel() {
    private val _symbol = MutableStateFlow("BTCUSDT")

    // stateIn keeps the connection alive for 5s during rotation
    val tickerData: StateFlow<String?> = _symbol.flatMapLatest {
        repository.getTickerFlow(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setSymbol(symbol: String) {
        _symbol.value = symbol
    }
}