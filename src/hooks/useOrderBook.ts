import { useEffect, useState, useRef } from 'react';
import OrderBookModule from '../../specs/NativeOrderBook';

export const useOrderBook = (symbol: string) => {
  // Note: Switched to number[][] to match our performance-first spec
  const [book, setBook] = useState<{ bids: number[][], asks: number[][] }>({ 
    bids: [], 
    asks: [] 
  });
  const timerRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    OrderBookModule.startOrderBook(symbol);

    // Poll at 100ms
    timerRef.current = setInterval(() => {
      // DATA FLOW: This call jumps directly into Kotlin memory via JSI
      const snapshot = OrderBookModule.getOrderBookSnapshot();
      
      if (snapshot && (snapshot.bids.length > 0 || snapshot.asks.length > 0)) {
        setBook(snapshot);
      }
    }, 100);

    return () => {
      OrderBookModule.stopOrderBook();
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [symbol]);

  return book;
};