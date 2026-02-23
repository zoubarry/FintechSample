const {getDefaultConfig, mergeConfig} = require('@react-native/metro-config');

const config = {
  resolver: {
    sourceExts: ['js', 'jsx', 'json', 'ts', 'tsx'], // Ensure ts/tsx are here
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);