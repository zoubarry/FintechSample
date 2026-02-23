import React, { memo } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useOrderBook } from '../hooks/useOrderBook';

// 1. Memoize the Row component. 
// This is CRITICAL for high-frequency updates so unchanged rows don't re-render.
const OrderBookRow = memo(({ price, amount, type }: { price: number, amount: number, type: 'bid' | 'ask' }) => {
    return (
        <View style={styles.row}>
            <Text style={[styles.priceText, { color: type === 'bid' ? '#4cd964' : '#ff3b30' }]}>
                {price.toFixed(2)}
            </Text>
            <Text style={styles.amountText}>{amount.toFixed(4)}</Text>
        </View>
    );
});

export const OrderBook = () => {
    const { bids, asks } = useOrderBook('BTCUSDT');

    return (
        <View style={styles.container}>
            {/* Table Header */}
            <View style={styles.headerRow}>
                <Text style={styles.headerLabel}>Price (USDT)</Text>
                <Text style={styles.headerLabel}>Amount (BTC)</Text>
            </View>

            {/* Asks (Sellers) - Showing top 10 */}
            <View style={styles.listSection}>
                {/* We reverse asks so the highest price is at the top, 
                    putting the "Best Ask" (lowest price) right above the spread */}
                {asks.slice(0, 10).reverse().map((item, i) => (
                    <OrderBookRow 
                        key={`ask-${i}`} 
                        price={item[0]} 
                        amount={item[1]} 
                        type="ask" 
                    />
                ))}
            </View>

            {/* Mid-Price / Spread Section */}
            <View style={styles.spreadContainer}>
                <Text style={styles.midPrice}>
                    {asks.length > 0 ? asks[0][0].toFixed(2) : '---'}
                </Text>
            </View>

            {/* Bids (Buyers) - Showing top 10 */}
            <View style={styles.listSection}>
                {bids.slice(0, 10).map((item, i) => (
                    <OrderBookRow 
                        key={`bid-${i}`} 
                        price={item[0]} 
                        amount={item[1]} 
                        type="bid" 
                    />
                ))}
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#000',
        paddingHorizontal: 15,
        paddingTop: 20,
    },
    headerRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingBottom: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#222',
    },
    headerLabel: {
        color: '#888',
        fontSize: 12,
        fontWeight: '600',
    },
    listSection: {
        flex: 1,
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        height: 25, // Fixed height is better for performance
        alignItems: 'center',
    },
    priceText: {
        fontSize: 14,
        fontWeight: 'bold',
        fontFamily: 'System', // Use monospace font if available for alignment
    },
    amountText: {
        color: '#fff',
        fontSize: 14,
    },
    spreadContainer: {
        paddingVertical: 15,
        alignItems: 'center',
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderColor: '#222',
        marginVertical: 5,
    },
    midPrice: {
        color: '#fff',
        fontSize: 22,
        fontWeight: 'bold',
    },
});