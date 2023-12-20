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

package com.frybits.project.provider.core.commands

import com.frybits.project.provider.core.di.affectedPathsModule
import com.frybits.project.provider.core.di.optionsModule
import com.frybits.project.provider.core.options.AffectedPathsOptions
import com.frybits.project.provider.core.options.ProjectProviderOptions
import com.frybits.project.provider.core.utils.VersionProvider
import com.squareup.affected.paths.core.CoreAnalyzer
import com.squareup.affected.paths.core.models.AnalysisResult
import com.squareup.affected.paths.core.utils.AffectedPathsMessage
import com.squareup.tooling.models.SquareProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.PrintWriter
import java.util.concurrent.Callable
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createParentDirectories

private val LOGGER = LoggerFactory.getLogger(ProjectProviderCommand::class.java)

@CommandLine.Command(
    name = "project-provider",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider::class,
    description = ["Provides information on paths that are affected by the given changes"],
    scope = CommandLine.ScopeType.INHERIT
)
internal class ProjectProviderCommand : Callable<Int> {

    @CommandLine.Spec
    private lateinit var spec: CommandLine.Model.CommandSpec

    @CommandLine.Mixin
    private val affectedPathsOptions = AffectedPathsOptions()

    @CommandLine.Mixin
    private val projectProviderOptions = ProjectProviderOptions()

    private val projectProviderApplication by lazy {
        val app = GlobalContext.getKoinApplicationOrNull() ?: startKoin(KoinApplication.init())
        return@lazy app.modules(
            optionsModule(affectedPathsOptions, projectProviderOptions),
            affectedPathsModule()
        )
    }

    override fun call(): Int {
        return with(projectProviderApplication) {
            try {
                return@with runBlocking {
                    val analysisResultAsync = async {
                        LOGGER.info("Awaiting analysis results")
                        val result = koin.get<CoreAnalyzer>().analyze()
                        LOGGER.info("Analysis results received")
                        return@async result
                    }

                    val affectedProjectsAsync =
                        async { determineProjectPathsAffectedBySources(analysisResultAsync.await()) }

                    launch { writeProjectPaths(affectedProjectsAsync.await()) }

                    return@runBlocking 0
                }
            } catch (e: Throwable) {
                if (e is AffectedPathsMessage) {
                    LOGGER.error(e.message)
                    return@with AffectedPathsMessage.AFFECTED_PATHS_ERROR_CODE
                } else {
                    LOGGER.error("Unable to complete analysis", e)
                }
                return@with spec.exitCodeOnExecutionException()
            }
        }
    }

    // Get projects affected by sources defined by all variants
    private suspend fun determineProjectPathsAffectedBySources(analysisResult: AnalysisResult): List<SquareProject> {
        LOGGER.info("Found the following changed files:")
        analysisResult.changedFiles.forEach { LOGGER.info("  $it") }
        return withContext(Dispatchers.Default) {
            return@withContext analysisResult.affectedResults.asSequence()
                .flatMap { affectedResult ->
                    val file = affectedResult.file
                    val affectedProjects = affectedResult.affectedProjectPaths.mapNotNull { analysisResult.projectMap[it] }
                    affectedProjects.filter { project ->
                        project.variants.any { (_, variant) -> variant.srcs.any { file.contains(it) } }
                    }
                }.toList()
        }
    }

    private suspend fun writeProjectPaths(affectedProjects: List<SquareProject>) {
        if (affectedProjects.isEmpty()) {
            LOGGER.warn("No affected projects found!")
            return
        }
        withContext(Dispatchers.IO) {
            val affectedProjectsPath = projectProviderOptions.affectedPathsOutput.normalize().createParentDirectories()

            LOGGER.info("Writing affected projects to $affectedProjectsPath")
            PrintWriter(affectedProjectsPath.bufferedWriter()).use { writer ->
                affectedProjects.asSequence()
                    .map { project ->
                        ":${project.pathToProject.replace('/', ':')}"
                    }
                    .distinct()
                    .forEach { projectPath ->
                        writer.println(projectPath)
                    }
                writer.flush()
            }
        }
    }
}
