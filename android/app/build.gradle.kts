import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.react.native)
}

react {
    nodeExecutableAndArgs.set(listOf("/usr/local/bin/node", "--no-warnings"))

    // 2. Explicitly point to the CLI - now TWO levels up (../../)
    cliFile.set(file("../../node_modules/react-native/cli.js"))

    // 3. Define your project root (where package.json lives)
    root.set(file("../../"))

    reactNativeDir.set(file("../../node_modules/react-native"))
    codegenDir.set(file("../../node_modules/@react-native/codegen"))

    // 4. (Optional) If you want to customize where the JS bundle is saved
    bundleAssetName.set("index.android.bundle")
}

android {
    namespace = "com.example.fintechsample"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.fintechsample"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        prefab = true
    }
    ndkVersion = "27.0.12077973"
}

dependencies {
    // --- Android Core & Compose ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.paging.compose)

    // --- Network & Images (NEW) ---
    implementation(libs.retrofit.core)  // <--- Note the name change
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging) // <--- Crucial for debugging
    implementation(libs.coil.compose)

    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.foundation.layout)
    ksp(libs.hilt.compiler) // Use KSP instead of kapt
    implementation(libs.hilt.navigation.compose) // For hiltViewModel()

    implementation("com.facebook.react:react-android")
    // Since 0.84 uses Hermes V1 by default, we need the engine too
    implementation("com.facebook.react:hermes-android")

    // --- Unit Testing (Local JVM) ---
    testImplementation(libs.junit)
    testImplementation(libs.mockk)      // <--- For mocking API
    testImplementation(libs.turbine)    // <--- For testing Flows
    testImplementation(libs.robolectric) // <--- For UI tests without emulator
    testImplementation(libs.kotlinx.coroutines.test)

    // --- Compose Testing for Robolectric (Local) ---
    // We need these in "testImplementation" so Robolectric can see them!
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Android Instrumentation Testing (Device) ---
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // --- Debugging ---
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}