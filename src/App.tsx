import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const App = () => (
  <View style={styles.container}>
    <Text style={styles.text}>React Native Screen Active mf!</Text>
  </View>
);

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#121212', justifyContent: 'center', alignItems: 'center' },
  text: { color: '#00FF00', fontSize: 20, fontWeight: 'bold' }
});

export default App;