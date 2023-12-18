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

package com.frybits.project.provider.core

import com.frybits.project.provider.core.commands.ProjectProviderCommand
import org.slf4j.LoggerFactory
import picocli.CommandLine
import kotlin.system.exitProcess
import kotlin.time.measureTimedValue

private val LOGGER = LoggerFactory.getLogger("com.frybits.project.provider.core.Main")

internal fun main(vararg args: String) {
    val (exitCode, duration) = measureTimedValue {
        val commandLine = CommandLine(ProjectProviderCommand())
        commandLine.isCaseInsensitiveEnumValuesAllowed = true
        val result = commandLine.execute(*args)
        commandLine.checkExitCode(result)
        return@measureTimedValue result
    }
    LOGGER.info("Process ran for $duration")
    exitProcess(exitCode)
}

// Does a quick exit code check, ensuring we don't hit any other code.
private fun CommandLine.checkExitCode(result: Int) {
    if (result == commandSpec.exitCodeOnInvalidInput()) {
        exitProcess(result)
    }

    if (isVersionHelpRequested || subcommands.any { (_, cm) -> cm.isVersionHelpRequested }) {
        exitProcess(commandSpec.exitCodeOnVersionHelp())
    }

    if (isUsageHelpRequested || subcommands.any { (_, cm) -> cm.isUsageHelpRequested }) {
        exitProcess(commandSpec.exitCodeOnUsageHelp())
    }
}
