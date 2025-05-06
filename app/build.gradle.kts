import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.android

val libs = the<LibrariesForLibs>()

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("kotlin-parcelize")
}


android {
    namespace = "com.jcmateus.casanarestereo"
    compileSdk = 35

    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\bylos\\Music\\Produccion_CasanareStereo\\CasanareStereo.jks")
            storePassword = "JCmateus07"
            keyAlias = "casanare stereo"
            keyPassword = "JCmateus22"
        }
    }

    defaultConfig {
        applicationId = "com.jcmateus.casanarestereo"
        minSdk = 25
        targetSdk = 34
        versionCode = 11
        versionName = "1.2.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        freeCompilerArgs += "-opt-in=androidx.navigation.compose.ExperimentalNavigationApi"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation.android)
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2")
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.ui.text.google.fonts)

    // Compose Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.runtime.saved.instance.state)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage.ktx)

    // Google Play Services
    implementation(libs.play.services.auth)
    implementation(libs.play.services.location)

    // YouTube Player
    implementation(libs.core)

    // ExoPlayer
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.extension.mediasession)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    // Material
    implementation(libs.androidx.material)
    implementation("com.google.android.material:material:1.12.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Kotlin datetime / coroutines
    implementation(libs.kotlinx.datetime)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Gson
    implementation(libs.gson)

    // Hilt
    //implementation(libs.hilt.android)

    // Accesibilidad
    implementation("com.google.android.apps.common.testing.accessibility.framework:accessibility-test-framework:4.1.1") {
        exclude(group = "androidx.test.espresso", module = "espresso-core")
    }
    implementation(libs.support.annotations)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.ui.test.android)
}
