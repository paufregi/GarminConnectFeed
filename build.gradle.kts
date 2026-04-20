plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serializable) apply false
    alias(libs.plugins.android.room) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}

repositories {
    google()
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
                    .using(module("io.netty:netty-codec-http2:4.1.132.Final"))
            }
            listOf("4.1.93.Final", "4.1.110.Final").forEach { version ->
                substitute(module("io.netty:netty-codec-http:$version"))
                    .using(module("io.netty:netty-codec-http:4.1.132.Final"))
            }
            substitute(module("org.apache.httpcomponents:httpclient:4.5.6"))
                .using(module("org.apache.httpcomponents:httpclient:4.5.13"))
            substitute(module("org.apache.commons:commons-lang3:3.16.0"))
                .using(module("org.apache.commons:commons-lang3:3.18.0"))
            substitute(module("org.bouncycastle:bcprov-jdk18on:1.79"))
                .using(module("org.bouncycastle:bcprov-jdk18on:1.84"))
            substitute(module("org.bouncycastle:bcpkix-jdk18on:1.79"))
                .using(module("org.bouncycastle:bcpkix-jdk18on:1.84"))
        }
    }
}

buildscript {
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("org.jdom:jdom2:2.0.6"))
                .using(module("org.jdom:jdom2:2.0.6.1"))
            substitute(module("org.bitbucket.b_c:jose4j:0.9.5"))
                .using(module("org.bitbucket.b_c:jose4j:0.9.6"))
            substitute(module("org.apache.httpcomponents:httpclient:4.5.6"))
                .using(module("org.apache.httpcomponents:httpclient:4.5.13"))
            substitute(module("org.apache.commons:commons-lang3:3.16.0"))
                .using(module("org.apache.commons:commons-lang3:3.18.0"))
            substitute(module("org.bouncycastle:bcprov-jdk18on:1.79"))
                .using(module("org.bouncycastle:bcprov-jdk18on:1.84"))
            substitute(module("org.bouncycastle:bcpkix-jdk18on:1.79"))
                .using(module("org.bouncycastle:bcpkix-jdk18on:1.84"))
        }
    }
}