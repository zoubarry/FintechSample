package com.example.fintechsample.ui.orderbook

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface OrderBookEntryPoint {
    fun orderBookRepository(): OrderBookRepository

    fun webSocketClient(): OrderBookWebSocketClient
}