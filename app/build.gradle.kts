plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val appId = "foo.bar.compose"
val composeVersion = rootProject.extra["composeVersion"] as String

android {

    compileSdk = 32

    defaultConfig {
        applicationId = appId
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
          kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {

    // reactivity
    implementation("co.early.fore:fore-kt-android:1.5.14")
    implementation("co.early.fore:fore-kt-android-compose:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")

    // compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.activity:activity-compose:1.4.0")

    // design
    implementation("com.google.android.material:material:1.6.1")

    // diagnostics
    implementation(kotlin("reflect"))
}
