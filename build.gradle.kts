import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serializable) apply false
    alias(libs.plugins.android.room) apply false
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
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.google.protobuf:protobuf-kotlin:3.24.4"))
                .using(module("com.google.protobuf:protobuf-kotlin:3.25.5"))
                .because("CVE-2024-7254 - Dependabot issue #24")
            substitute(module("io.netty:netty-codec-http2:4.1.110.Final"))
                .using(module("io.netty:netty-codec-http2:4.1.124.Final"))
                .because("CVE-2023-44487 - Dependabot issue #25")
            substitute(module("io.netty:netty-codec-http2:4.1.93.Final"))
                .using(module("io.netty:netty-codec-http2:4.1.124.Final"))
                .because("CVE-2023-44487 - Dependabot issue #25")
        }
    }
}

buildscript {
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("org.jdom:jdom2:2.0.6"))
                .using(module("org.jdom:jdom2:2.0.6.1"))
                .because("Dependabot issue #28")
            substitute(module("org.bitbucket.b_c:jose4j:0.9.5"))
                .using(module("org.bitbucket.b_c:jose4j:0.9.6"))
                .because("Dependabot issue #30")
        }
    }
}

repositories {
    google()
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}