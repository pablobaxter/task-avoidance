package com.frybits.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

class FrybitsMultiPlatformPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.run {
        apply<KotlinMultiplatformPluginWrapper>()
    }
}
