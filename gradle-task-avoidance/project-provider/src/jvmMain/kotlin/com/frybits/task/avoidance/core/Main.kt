package com.frybits.task.avoidance.core

import com.frybits.task.avoidance.core.commands.TaskAvoidanceCommand
import org.slf4j.LoggerFactory
import picocli.CommandLine
import kotlin.system.exitProcess
import kotlin.time.measureTimedValue

private val LOGGER = LoggerFactory.getLogger("com.frybits.task.avoidance.core.Main")

internal fun main(vararg args: String) {
    val (exitCode, duration) = measureTimedValue {
        val commandLine = CommandLine(TaskAvoidanceCommand())
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
