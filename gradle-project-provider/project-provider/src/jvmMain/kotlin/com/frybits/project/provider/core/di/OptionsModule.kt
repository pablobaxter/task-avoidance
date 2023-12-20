package com.frybits.project.provider.core.di

import com.frybits.project.provider.core.options.AffectedPathsOptions
import com.frybits.project.provider.core.options.ProjectProviderOptions
import com.frybits.project.provider.core.utils.toCoreOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal enum class OptionKeys {
    AFFECTED_PATHS_OUTPUT
}

internal fun optionsModule(affectedPathsOptions: AffectedPathsOptions, projectProviderOptions: ProjectProviderOptions) =
    module {
        single { affectedPathsOptions.toCoreOptions() }
        single(named(OptionKeys.AFFECTED_PATHS_OUTPUT)) { projectProviderOptions.affectedPathsOutput }
    }
