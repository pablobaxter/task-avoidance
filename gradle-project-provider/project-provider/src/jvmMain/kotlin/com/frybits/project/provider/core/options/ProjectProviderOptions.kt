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

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.frybits.project.provider.core.utils.LogBackLogLevelTypeConverter
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory
import picocli.CommandLine.Option
import java.nio.file.Path
import kotlin.io.path.*

/**
 * Configuration flags for Project-Provider
 */
@Suppress("unused")
internal class ProjectProviderOptions {

    @Option(
        names = ["--logging"],
        description = ["Logging level (OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL)"],
        converter = [LogBackLogLevelTypeConverter::class],
        defaultValue = "info"
    )
    private fun setLogLevel(level: Level) {
        val logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME) as Logger
        logger.level = level
    }

    @Option(
        names = ["--affected-projects-output"],
        description = ["File to output affected project paths"]
    )
    var affectedPathsOutput: Path = Path("affected-projects.out")
}
