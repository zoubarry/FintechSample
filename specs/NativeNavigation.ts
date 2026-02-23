import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  // Use 'string' (lowercase) for TypeScript primitive types
  navigateToOrderBook(symbol: string): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('NavigationModule');