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
