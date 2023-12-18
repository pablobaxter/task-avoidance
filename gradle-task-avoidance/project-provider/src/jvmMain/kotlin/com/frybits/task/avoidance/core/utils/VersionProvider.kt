package com.frybits.task.avoidance.core.utils

import picocli.CommandLine

/**
 * Provides the version information for all commands
 */
internal class VersionProvider : CommandLine.IVersionProvider {
    override fun getVersion(): Array<String> {
        return arrayOf(
            "gradle-task-avoidance: ${javaClass.`package`.implementationVersion}".colorized(Color.YELLOW),
            "Picocli: ${CommandLine.VERSION}".colorized(Color.RED),
            "JVM: \${java.version} (\${java.vendor} \${java.vm.name} \${java.vm.version})".colorized(Color.BLUE),
            "OS: \${os.name} \${os.version} \${os.arch}".colorized(Color.GREEN)
        )
    }
}

private enum class Color {
    YELLOW, BLUE, RED, GREEN
}

private fun String.colorized(color: Color): String {
    return "@|${color.name.lowercase()} $this|@"
}
