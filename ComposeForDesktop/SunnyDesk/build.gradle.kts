import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    id("org.jetbrains.compose") version "0.4.0"
}

group = "com.raywenderlich"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("io.ktor:ktor-client-core:1.6.4")
    implementation("io.ktor:ktor-client-cio:1.6.4")
    implementation("io.ktor:ktor-client-serialization:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SunnyDesk"
            packageVersion = "1.0.0"
        }
    }
}
