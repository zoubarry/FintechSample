import React, { useEffect, useState } from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    ScrollView,
    StyleSheet,
    NativeModules,
    NativeEventEmitter
} from 'react-native';

// Import the TurboModule Spec we just created
import NativeNavigation from '../../specs/NativeNavigation';

const { TickerModule } = NativeModules;
const tickerEmitter = new NativeEventEmitter(TickerModule);

const SYMBOLS = ['BTCUSDT', 'ETHUSDT', 'SOLUSDT', 'BNBUSDT', 'ADAUSDT'];

interface TickerData {
    s: string; // Symbol
    c: string; // Price
    P: string; // Percent Change
}

export const TickerScreen = () => {
    const [activeSymbol, setActiveSymbol] = useState('BTCUSDT');
    const [ticker, setTicker] = useState<TickerData | null>(null);

    useEffect(() => {
        console.log('Commanding Native to start ticker for:', activeSymbol);
        if (TickerModule && TickerModule.startTicker) {
            TickerModule.startTicker(activeSymbol);
        }

        const sub = tickerEmitter.addListener('onTickerUpdate', (json: string) => {
            setTicker(JSON.parse(json));
        });

        return () => sub.remove();
    }, [activeSymbol]);

    const isUp = ticker ? parseFloat(ticker.P) >= 0 : true;

    return (
        <View style={styles.container}>
            {/* Selector Header */}
            <View style={styles.header}>
                <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
                    {SYMBOLS.map((s) => (
                        <TouchableOpacity
                            key={s}
                            onPress={() => {
                                setTicker(null);
                                setActiveSymbol(s);
                            }}
                            style={[styles.chip, activeSymbol === s && styles.activeChip]}
                        >
                            <Text style={[styles.chipText, activeSymbol === s && styles.activeChipText]}>{s}</Text>
                        </TouchableOpacity>
                    ))}
                </ScrollView>
            </View>

            {/* Main Display */}
            <View style={styles.display}>
                <Text style={styles.symbolLabel}>{activeSymbol}</Text>
                <Text style={styles.priceText}>
                    {ticker ? `$${parseFloat(ticker.c).toLocaleString(undefined, { minimumFractionDigits: 2 })}` : '---'}
                </Text>
                {ticker && (
                    <Text style={[styles.changeText, { color: isUp ? '#4cd964' : '#ff3b30' }]}>
                        {isUp ? '▲' : '▼'} {ticker.P}%
                    </Text>
                )}

                {/* NEW: Navigation Trigger Button */}
                <TouchableOpacity 
                    style={styles.orderBookButton}
                    onPress={() => {
                        console.log('Navigating to Order Book for:', activeSymbol);
                        // Using our New Architecture Spec
                        NativeNavigation.navigateToOrderBook(activeSymbol);
                    }}
                >
                    <Text style={styles.buttonText}>View Order Book</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    // ... your existing styles ...
    container: { flex: 1, backgroundColor: '#000' },
    header: { paddingTop: 50, paddingBottom: 20, backgroundColor: '#111' },
    scrollContent: { paddingHorizontal: 15 },
    chip: { paddingHorizontal: 20, paddingVertical: 10, borderRadius: 20, backgroundColor: '#222', marginRight: 10, borderWidth: 1, borderColor: '#333' },
    activeChip: { backgroundColor: '#F3BA2F', borderColor: '#F3BA2F' },
    chipText: { color: '#888', fontWeight: 'bold' },
    activeChipText: { color: '#000' },
    display: { flex: 1, justifyContent: 'center', alignItems: 'center' },
    symbolLabel: { color: '#888', fontSize: 20, letterSpacing: 2, marginBottom: 10 },
    priceText: { color: '#fff', fontSize: 56, fontWeight: 'bold' },
    changeText: { fontSize: 24, fontWeight: '600', marginTop: 10 },
    
    // NEW: Style for the Order Book Button
    orderBookButton: {
        marginTop: 40,
        paddingHorizontal: 30,
        paddingVertical: 15,
        backgroundColor: '#333',
        borderRadius: 12,
        borderWidth: 1,
        borderColor: '#444',
    },
    buttonText: {
        color: '#F3BA2F', // Binance Yellow text
        fontSize: 16,
        fontWeight: 'bold',
    }
});