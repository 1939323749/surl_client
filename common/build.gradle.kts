plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

group = "cn.snowlie.app"
version = "1.0-SNAPSHOT"

val ktorversion = "2.3.2"

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation("io.ktor:ktor-client-core:$ktorversion")
                implementation("io.ktor:ktor-client-json:$ktorversion")
                implementation("io.ktor:ktor-client-serialization:$ktorversion")
                implementation("io.ktor:ktor-client-logging:$ktorversion")
                implementation("io.ktor:ktor-client-cio:$ktorversion")
                implementation("io.ktor:ktor-client-json-jvm:$ktorversion")
                implementation("androidx.compose.material3:material3:1.0.1")
                implementation("androidx.compose.material3:material3-window-size-class:1.0.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.9.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdkVersion(33)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(33)
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}