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

import com.frybits.project.provider.core.utils.SquareDependencySerializer
import com.frybits.project.provider.core.utils.SquareProjectSerializer
import com.frybits.project.provider.core.utils.SquareTestConfigurationSerializer
import com.frybits.project.provider.core.utils.SquareVariantConfigurationSerializer
import com.squareup.affected.paths.core.CoreAnalyzer
import com.squareup.tooling.models.SquareDependency
import com.squareup.tooling.models.SquareProject
import com.squareup.tooling.models.SquareTestConfiguration
import com.squareup.tooling.models.SquareVariantConfiguration
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.dsl.module

private val projectModule = SerializersModule {
    polymorphicDefaultDeserializer(SquareDependency::class) { SquareDependencySerializer }
    polymorphicDefaultSerializer(SquareDependency::class) { SquareDependencySerializer }

    polymorphicDefaultDeserializer(SquareTestConfiguration::class) { SquareTestConfigurationSerializer }
    polymorphicDefaultSerializer(SquareTestConfiguration::class) { SquareTestConfigurationSerializer }

    polymorphicDefaultDeserializer(SquareVariantConfiguration::class) { SquareVariantConfigurationSerializer }
    polymorphicDefaultSerializer(SquareVariantConfiguration::class) { SquareVariantConfigurationSerializer }

    polymorphicDefaultDeserializer(SquareProject::class) { SquareProjectSerializer }
    polymorphicDefaultSerializer(SquareProject::class) { SquareProjectSerializer }
}

internal fun affectedPathsModule() = module {
    single { CoreAnalyzer(get()) }
    single {
        Json {
            prettyPrint = false
            isLenient = false
            explicitNulls = true
            serializersModule = projectModule
        }
    }
}
