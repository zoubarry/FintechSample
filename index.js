import { AppRegistry } from 'react-native';
import { TickerScreen } from './src/screens/TickerScreen';
import { OrderBook } from './src/components/OrderBook';

AppRegistry.registerComponent('CryptoTickerApp', () => TickerScreen);
AppRegistry.registerComponent('OrderBook', () => OrderBook);