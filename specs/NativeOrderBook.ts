// specs/NativeOrderBook.ts
import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  // Methods are async by default in TurboModules (return void/Promise)
  startOrderBook(symbol: string): void;
  stopOrderBook(): void;
  
  // A Synchronous method! This is the TurboModule superpower.
  // JS can query the top price instantly without waiting for an event.
  getBestBidSync(): string;

  // enhancement
  getOrderBookSnapshot(): {
      bids: number[][],
      asks: number[][]
  };
}

export default TurboModuleRegistry.getEnforcing<Spec>('OrderBookModule');