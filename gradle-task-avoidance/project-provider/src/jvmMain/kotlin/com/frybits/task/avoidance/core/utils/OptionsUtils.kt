package com.frybits.task.avoidance.core.utils

import com.frybits.task.avoidance.core.options.TaskAvoidanceOptions
import com.squareup.affected.paths.core.CoreOptions

internal fun TaskAvoidanceOptions.toCoreOptions(): CoreOptions {
    return CoreOptions(
        logGradle = logGradle,
        directory = directory,
        comparisonCommit = comparisonCommit,
        debugGradle = debugGradle,
        allowGradleParallel = allowGradleParallel,
        initialGradleMemory = initialGradleMemory,
        maxGradleMemory = maxGradleMemory,
        customJvmFlags = listOf("-XX:-MaxFDLimit"),
        customGradleFlags = listOf("--stacktrace"),
        autoInjectPlugin = autoInject,
        changedFiles = changedFiles,
        gradleInstallationPath = gradleInstallationPath,
    )
}
