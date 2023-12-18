package com.frybits.task.avoidance.core.utils

import ch.qos.logback.classic.Level
import picocli.CommandLine

internal class LogBackLogLevelTypeConverter : CommandLine.ITypeConverter<Level> {

    override fun convert(value: String?): Level {
        return Level.toLevel(value)
    }
}
