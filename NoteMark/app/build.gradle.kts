import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("de.mannodermaus.android-junit5") version "1.13.1.0"
}

val localProperties = Properties().apply {
    load(File(rootProject.projectDir, "local.properties").inputStream())
}

android {
    namespace = "com.joshayoung.notemark"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.joshayoung.notemark"
        minSdk = 26
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
        debug {
            buildConfigField(
                "String",
                "CAMPUS_SUBSCRIPTION_EMAIL",
                localProperties.getProperty("CAMPUS_SUBSCRIPTION_EMAIL")
            )
            buildConfigField(
                "String",
                "BASE_URL",
                localProperties.getProperty("BASE_URL")
            )
            buildConfigField(
                "String",
                "REGISTER_PATH",
                localProperties.getProperty("REGISTER_PATH")
            )
            buildConfigField(
                "String",
                "REFRESH_PATH",
                localProperties.getProperty("REFRESH_PATH")
            )
            buildConfigField(
                "String",
                "NOTE_PATH",
                localProperties.getProperty("NOTE_PATH")
            )
            buildConfigField(
                "String",
                "LOGIN_PATH",
                localProperties.getProperty("LOGIN_PATH")
            )
            buildConfigField(
                "String",
                "LOGOUT_PATH",
                localProperties.getProperty("LOGOUT_PATH")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.security.crypto.ktx)

    implementation(libs.androidx.datastore.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.datastore.preferences)

    implementation(libs.symbol.processing)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.assertk)

    implementation(libs.material3.adaptive)

}