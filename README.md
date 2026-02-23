# High-Performance Hybrid Order Book (RN 0.83+)

A real-time cryptocurrency order book built with **React Native Bridgeless Mode** and **TurboModules**. This project demonstrates how to handle high-frequency market data (100ms updates) by bypassing the legacy bridge in favor of a synchronous **JSI Pull** architecture.

## 🚀 Architectural Highlights
### 1. Synchronous JSI Pull (Snapshot Pattern)
### 2. Thread-Safe Kotlin Repository
### 3. UI Optimization

---
## 🛠 Tech Stack
* **Language:** Kotlin (100% Native Android Logic), TypeScript (React Native)
* **Framework:** React Native 0.83 (New Architecture, Bridgeless, Codegen)
* **UI:** Jetpack Compose (App Shell/Navigation), React Native (Market Screens)
* **Network:** Binance WebSocket API

## 📦 Project Structure
- `android/`: Native Kotlin modules and Ticker/OrderBook repositories.
- `specs/`: TurboModule TypeScript specifications (The JSI Contract).
- `src/`: React Native UI components and custom high-frequency hooks.

---

## 🏗 Setup & Installation
1. Install dependencies: `npm install`
2. Generate TurboModule glue code: `cd android && ./gradlew generateCodegenArtifactsFromSchema`
3. Run the app: `npx react-native run-android`

## 📝 License
MIT
