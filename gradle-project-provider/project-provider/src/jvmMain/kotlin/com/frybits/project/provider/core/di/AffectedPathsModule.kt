package com.frybits.project.provider.core.di

import com.squareup.affected.paths.core.CoreAnalyzer
import org.koin.dsl.module

internal fun affectedPathsModule() = module {
    single { CoreAnalyzer(get()) }
}
