plugins {
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.kotlin.plugin)
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.shadow.jar.plugin)
}

gradlePlugin {
    plugins {
        create("frybitsAppPlugin") {
            id = "frybits-mpp"
            implementationClass = "com.frybits.gradle.FrybitsMultiPlatformPlugin"
        }
    }
}
