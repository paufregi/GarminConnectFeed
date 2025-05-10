plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serializable) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.dependency.analysis) apply false
    alias(libs.plugins.dependency.versions)
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}
repositories {
    google()
}
