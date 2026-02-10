import com.android.build.api.dsl.ManagedVirtualDevice
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.room)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serializable)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.dependency.analysis)
}

android {
    namespace = "paufregi.connectfeed"
    compileSdk = 36

    defaultConfig {
        applicationId = "paufregi.connectfeed"
        minSdk = 33
        targetSdk = 36
        versionCode = 32
        versionName = "2.4.5"

        testInstrumentationRunner = "paufregi.connectfeed.TestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }

        buildConfigField("String", "GARMIN_CONSUMER_KEY", "\"${properties.getProperty("garmin.consumer_key", "GARMIN_CONSUMER_KEY")}\"")
        buildConfigField("String", "GARMIN_CONSUMER_SECRET", "\"${properties.getProperty("garmin.consumer_secret", "GARMIN_CONSUMER_SECRET")}\"")

        buildConfigField("String", "STRAVA_CLIENT_ID", "\"${properties.getProperty("strava.client_id", "STRAVA_CLIENT_ID")}\"")
        buildConfigField("String", "STRAVA_CLIENT_SECRET", "\"${properties.getProperty("strava.client_secret", "STRAVA_CLIENT_SECRET")}\"")
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
        debug {
            isDebuggable = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
        sourceSets.all {
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.8.0"
    }

    lint {
        disable += "OldTargetApi"
    }

    testOptions {
        animationsDisabled = true
        managedDevices {
            allDevices {
                register("pixel9Pro", ManagedVirtualDevice::class) {
                    device = "Pixel 9 Pro"
                    apiLevel = 36
                    systemImageSource = "aosp"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.signpost)
    implementation(libs.java.jwt)
    implementation(libs.fit)
    implementation(libs.commons.csv)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)

    testRuntimeOnly(libs.androidx.test.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.turbine)

    androidTestImplementation(libs.android.core.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.hilt.testing)
    androidTestImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.okhttp.tls)
    androidTestImplementation(libs.turbine)
    
    kspAndroidTest(libs.hilt.compiler)
}
