version = file("version.txt").readText().trim()
group = "com.proguard.proguard-core-visualizer"

plugins {
    kotlin("multiplatform") version libs.versions.kotlin.multiplatform
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jlleitschuh.ktlint)
    alias(libs.plugins.conveyor)
}

repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        withJava()
    }
    jvmToolchain(17)

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                api(compose.material3)
                api(compose.materialIconsExtended)
                implementation(libs.google.gson)
                implementation(libs.materialkolor)
                implementation(libs.dansoftowner.jthemedetecor)
                implementation("com.github.Guardsquare.proguard-core:proguard-core:master-SNAPSHOT")
                implementation(libs.proguard.core.android)
                implementation(libs.proguard.assembler)


                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

dependencies {
    // Use the configurations created by the Conveyor plugin to tell Gradle/Conveyor where to find the artifacts for each platform.
    linuxAmd64(compose.desktop.linux_x64)
    macAmd64(compose.desktop.macos_x64)
    macAarch64(compose.desktop.macos_arm64)
    windowsAmd64(compose.desktop.windows_x64)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            vendor = "GuardSquare"
            description = " A Compose Multiplatform Desktop application for visualizing the ProGuardCORE jbc evaluation "
        }
    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set(libs.versions.ktlint)
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(true)
}

configurations.all {
    // Work around temporary Compose bugs.
    // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
    attributes {
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
