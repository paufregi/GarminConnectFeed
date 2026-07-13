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
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

android {
    namespace = "paufregi.connectfeed"
    compileSdk = 37

    defaultConfig {
        applicationId = "paufregi.connectfeed"
        minSdk = 33
        targetSdk = 37
        versionCode = 38
        versionName = "2.4.11"

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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testFixtures {
        enable = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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

    sourceSets {
        getByName("testFixtures") {
            resources.directories.add("src/main/res")
        }
    }
}

dependencies {
    api(libs.jwt.kt)

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
    implementation(libs.fit)
    implementation(libs.commons.csv)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugRuntimeOnly(libs.androidx.ui.test.manifest)

    testImplementation(testFixtures(project(":app")))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)

    androidTestImplementation(testFixtures(project(":app")))
    androidTestImplementation(libs.android.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.hilt.testing)
    
    testFixturesApi(libs.kotlinx.coroutines.test)
    testFixturesApi(libs.turbine)
    testFixturesApi(libs.truth)
    testFixturesApi(libs.androidx.compose.runtime)
    testFixturesApi(libs.okhttp.mockwebserver)
    testFixturesApi(libs.okhttp.tls)
    testFixturesApi(libs.androidx.test.core)
}
