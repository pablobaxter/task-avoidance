plugins {
    id("frybits-mpp")
    id("application")
}

kotlin {
    jvm()
    sourceSets {
        jvmMain {
            dependencies {
                implementation(libs.affected.paths.core)
                implementation(libs.picocli.core)
                implementation(libs.logback.classic)
                implementation(libs.logback.core)
                implementation(libs.slf4j.api)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
