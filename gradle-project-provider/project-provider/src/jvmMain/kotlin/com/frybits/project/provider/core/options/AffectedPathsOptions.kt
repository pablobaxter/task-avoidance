/*
 *  Copyright 2023 Pablo Baxter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Created by Pablo Baxter (Github: pablobaxter)
 * https://github.com/pablobaxter/task-avoidance
 */

package com.frybits.project.provider.core.options

import picocli.CommandLine.Option
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.isDirectory

/**
 * Configuration flags for Affected-Paths
 */
class AffectedPathsOptions {

    @Option(
        names = ["--log-gradle"],
        description = ["Output Gradle logs"],
        fallbackValue = "true"
    )
    var logGradle: Boolean = true
        internal set

    @Option(
        names = ["--debug-gradle"],
        description = ["Makes the Gradle daemon used by TAPI debuggable"],
        fallbackValue = "true"
    )
    var debugGradle: Boolean = false
        internal set

    @set:Option(
        names = ["--dir"],
        description = ["Gradle build directory"],
        defaultValue = "."
    )
    var directory: Path = Path(".").normalize().toRealPath()
        internal set(value) {
            val p = value.normalize().toRealPath()
            require(p.isDirectory()) { "Param --dir must point to a valid directory" }
            field = p
        }

    @set:Option(
        names = ["--comparison-commit"],
        description = ["The commit hash to compare against for git diff"],
        defaultValue = ""
    )
    lateinit var comparisonCommit: String
        internal set

    @Option(
        names = ["--allow-gradle-parallel"],
        description = ["Allows parallel configuration of Gradle builds"],
    )
    var allowGradleParallel: Boolean = true
        internal set

    @Option(
        names = ["--gradle-initial-memory"],
        description = [
            "Sets the initial JVM memory size for Gradle daemon (in MB)",
            "Equivalent to using '-Xms' for the Gradle JVM args"
        ]
    )
    var initialGradleMemory: Int? = null
        internal set

    @Option(
        names = ["--gradle-max-memory"],
        description = [
            "Sets the max JVM memory size for Gradle daemon (in MB)",
            "Equivalent to using '-Xmx' for the Gradle JVM args"
        ]
    )
    var maxGradleMemory: Int? = null
        internal set

    @Option(
        names = ["--inject-plugin"],
        description = ["Injects the \"com.squareup.tooling\" plugin to all projects in the build"]
    )
    var autoInject: Boolean = false
        internal set

    @Option(
        names = ["--changed-file", "-f"],
        description = ["Changed files to use instead of the Git diff"]
    )
    var changedFiles: List<String> = emptyList()
        internal set

    @Option(
        names = ["--gradle-installation-path"],
        description = ["Use a custom Gradle installation"]
    )
    var gradleInstallationPath: Path? = null
        internal set

    @Option(
        names = ["--gradle-jvm-options"],
        description = ["List of Gradle JVM options to pass"]
    )
    var jvmOptions: List<String> = emptyList()
        internal set

    @Option(
        names = ["--gradle-flags"],
        description = ["List of Gradle flags to pass"]
    )
    var gradleFlags: List<String> = emptyList()
        internal set

    @Option(
        names = ["--use-included-builds"],
        description = ["Use the included builds as defined in the settings.gradle"]
    )
    var useIncludedBuilds: Boolean = false
        internal set
}
