import java.net.URI

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = URI("https://repo.gradle.org/gradle/libs-releases") }
    }
}

rootProject.name = "com.frybits"
include(":project-provider")
