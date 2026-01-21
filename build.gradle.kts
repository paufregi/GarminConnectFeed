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
            listOf("4.1.93.Final", "4.1.110.Final").forEach { version ->
                substitute(module("io.netty:netty-codec-http2:$version"))
                    .using(module("io.netty:netty-codec-http2:4.1.130.Final"))
            }
            substitute(module("com.google.protobuf:protobuf-kotlin:3.24.4"))
                .using(module("com.google.protobuf:protobuf-kotlin:3.25.5"))
            substitute(module("org.apache.httpcomponents:httpclient:4.5.6"))
                .using(module("org.apache.httpcomponents:httpclient:4.5.13"))
            substitute(module("org.apache.commons:commons-lang3:3.16.0"))
                .using(module("org.apache.commons:commons-lang3:3.18.0"))
        }
    }
}

buildscript {
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("org.jdom:jdom2:2.0.6.1"))
                .using(module("org.jdom:jdom2:2.0.6.1.1"))
            substitute(module("org.bitbucket.b_c:jose4j:0.9.5"))
                .using(module("org.bitbucket.b_c:jose4j:0.9.6"))
            substitute(module("org.apache.httpcomponents:httpclient:4.5.6"))
                .using(module("org.apache.httpcomponents:httpclient:4.5.13"))
            substitute(module("org.apache.commons:commons-lang3:3.16.0"))
                .using(module("org.apache.commons:commons-lang3:3.18.0"))
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