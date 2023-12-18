plugins {
    id("frybits-mpp")
    id("application")
}

application {
    mainClass.set("com.frybits.project.provider.core.MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes("Implementation-Version" to project.version)
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_11.majorVersion))
        }
        withJava()
    }
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
