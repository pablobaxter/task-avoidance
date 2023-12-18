package com.frybits.task.avoidance.core.commands

import com.frybits.task.avoidance.core.options.TaskAvoidanceOptions
import com.frybits.task.avoidance.core.utils.VersionProvider
import com.frybits.task.avoidance.core.utils.toCoreOptions
import com.squareup.affected.paths.core.CoreAnalyzer
import com.squareup.affected.paths.core.utils.AffectedPathsMessage
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.util.concurrent.Callable

private val LOGGER = LoggerFactory.getLogger(TaskAvoidanceCommand::class.java)

@CommandLine.Command(
    name = "task-avoidance",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider::class,
    description = ["Performs the necessary avoidance task"],
    scope = CommandLine.ScopeType.INHERIT
)
internal class TaskAvoidanceCommand : Callable<Int> {

    @CommandLine.Spec
    private lateinit var spec: CommandLine.Model.CommandSpec

    @CommandLine.Mixin
    private val options = TaskAvoidanceOptions()

    private val coreAnalyzer by lazy { CoreAnalyzer(options.toCoreOptions()) }

    override fun call(): Int {
        try {
            return runBlocking {
                val analysisResult = coreAnalyzer.analyze()

                return@runBlocking 0
            }
        } catch (e: Throwable) {
            if (e is AffectedPathsMessage) {
                LOGGER.error(e.message)
                return AffectedPathsMessage.AFFECTED_PATHS_ERROR_CODE
            } else {
                LOGGER.error("Unable to complete analysis", e)
            }
            return spec.exitCodeOnExecutionException()
        }
    }
}
