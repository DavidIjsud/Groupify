plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.palmyrasoft.groupify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.palmyrasoft.groupify"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "1.0.5"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // WorkManager — required in the app module so that:
    //   1. androidx.work.impl.foreground.SystemForegroundService resolves in AndroidManifest.xml
    //   2. androidx.startup.InitializationProvider resolves in AndroidManifest.xml
    //      (startup-runtime is a transitive dep of work-runtime-ktx)
    //   3. HiltWorkerFactory compiles in GroupifyApp.kt
    // The feature module's `implementation` scope does NOT expose these to the app module.
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    // Splash screen
    implementation("androidx.core:core-splashscreen:1.2.0")
    // Feature modules
    implementation(project(":feature:personalbum"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}