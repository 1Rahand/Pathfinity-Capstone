import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.osdetector)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrainsCompose.hotReload)
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass.set("MainKt")
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

room{
    schemaDirectory("$projectDir/schemas")
}

dependencies{
    ksp(libs.androidx.room.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        iosMain.dependencies {
            implementation(libs.ktor.darwin)
            implementation(libs.cryptography.provider.apple)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.webkit)
            implementation(libs.ktor.cio)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.cryptography.provider.jdk)
            implementation(libs.koin.android)
            implementation(libs.rootbeer.lib)
            implementation(libs.agora.full.sdk)

            implementation(libs.agora.uikit)

        }

        commonMain.dependencies {
            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)



            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)

            implementation(libs.navigation.compose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)


            implementation(libs.coil.core)
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.ktor)
//            implementation(libs.coil.gif)

            // Enables FileKit without Compose dependencies
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogscompose)
            implementation(libs.filekit.coil)

            // Enables FileKit with Composable utilities

            api(libs.compose.webview.multiplatform)

            implementation(libs.composeIcons.tablerIcons)
            implementation(libs.composeIcons.simpleIcons)
            implementation(libs.composeIcons.fontAwesome)
            implementation(libs.composeIcons.evaIcons)

            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.supabase.postgres)
            implementation(libs.supabase.realtime)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.storage)
            implementation(libs.kotlinx.serialization)

            implementation(libs.compottie)

            implementation(project.dependencies.platform(libs.cryptography.bom))
            implementation(libs.cryptography.core)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

            implementation(libs.alert.kmp)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-annotations-common"))
            implementation(libs.assertk)

            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            implementation(libs.ktor.cio)
            implementation(libs.slf4j.api)
            implementation(libs.logback.classic)
            implementation(libs.cryptography.provider.jdk)


            val fxSuffix = when (osdetector.classifier) {
                "linux-x86_64" -> "linux"
                "linux-aarch_64" -> "linux-aarch64"
                "windows-x86_64" -> "win"
                "osx-x86_64" -> "mac"
                "osx-aarch_64" -> "mac-aarch64"
                else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
            }
            implementation("org.openjfx:javafx-base:17:${fxSuffix}")
            implementation("org.openjfx:javafx-graphics:17:${fxSuffix}")
            implementation("org.openjfx:javafx-controls:17:${fxSuffix}")
            implementation("org.openjfx:javafx-swing:17:${fxSuffix}")
            implementation("org.openjfx:javafx-web:17:${fxSuffix}")
            implementation("org.openjfx:javafx-media:17:${fxSuffix}")
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "krd.enos.pathfinity"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "krd.enos.pathfinity"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "krd.enos.pathfinity"
            packageVersion = "1.0.0"
        }
    }
}
