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

package com.frybits.project.provider.core.utils

import com.squareup.tooling.models.SquareDependency
import com.squareup.tooling.models.SquareProject
import com.squareup.tooling.models.SquareTestConfiguration
import com.squareup.tooling.models.SquareVariantConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure

internal object SquareProjectSerializer : KSerializer<SquareProject> {

    override val descriptor: SerialDescriptor = SquareProjectImpl.serializer().descriptor

    override fun deserialize(decoder: Decoder): SquareProject {
        return SquareProjectImpl.serializer().deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, value: SquareProject) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, descriptor.getElementIndex("name"), value.name)
            encodeStringElement(descriptor, descriptor.getElementIndex("namespace"), value.namespace)
            encodeStringElement(descriptor, descriptor.getElementIndex("pathToProject"), value.pathToProject)
            encodeStringElement(descriptor, descriptor.getElementIndex("pluginUsed"), value.pluginUsed)
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("variants"),
                MapSerializer(String.serializer(), SquareVariantConfigurationSerializer),
                value.variants
            )
        }
    }
}

@Serializable
@SerialName("SquareProject")
private data class SquareProjectImpl(
    override val name: String,
    override val namespace: String,
    override val pathToProject: String,
    override val pluginUsed: String,
    override val variants: Map<String, SquareVariantConfiguration>
) : SquareProject

internal object SquareVariantConfigurationSerializer : KSerializer<SquareVariantConfiguration> {

    override val descriptor: SerialDescriptor = SquareVariantConfigurationImpl.serializer().descriptor

    override fun deserialize(decoder: Decoder): SquareVariantConfiguration {
        return SquareVariantConfigurationImpl.serializer().deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, value: SquareVariantConfiguration) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("deps"),
                SetSerializer(SquareDependencySerializer),
                value.deps
            )
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("srcs"),
                SetSerializer(String.serializer()),
                value.srcs
            )
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("tests"),
                MapSerializer(String.serializer(), SquareTestConfigurationSerializer),
                value.tests
            )
        }
    }
}

@Serializable
@SerialName("SquareVariantConfiguration")
private data class SquareVariantConfigurationImpl(
    override val deps: Set<SquareDependency>,
    override val srcs: Set<String>,
    override val tests: Map<String, SquareTestConfiguration>
) : SquareVariantConfiguration

internal object SquareTestConfigurationSerializer : KSerializer<SquareTestConfiguration> {

    override val descriptor: SerialDescriptor = SquareTestConfigurationImpl.serializer().descriptor

    override fun deserialize(decoder: Decoder): SquareTestConfiguration {
        return SquareTestConfigurationImpl.serializer().deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, value: SquareTestConfiguration) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("deps"),
                SetSerializer(SquareDependencySerializer),
                value.deps
            )
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("srcs"),
                SetSerializer(String.serializer()),
                value.srcs
            )
        }
    }
}

@Serializable
@SerialName("SquareTestConfiguration")
private data class SquareTestConfigurationImpl(
    override val deps: Set<SquareDependency>,
    override val srcs: Set<String>
) : SquareTestConfiguration

internal object SquareDependencySerializer : KSerializer<SquareDependency> {

    override val descriptor: SerialDescriptor = SquareDependencyImpl.serializer().descriptor

    override fun deserialize(decoder: Decoder): SquareDependency {
        return SquareDependencyImpl.serializer().deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, value: SquareDependency) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                descriptor.getElementIndex("tags"),
                SetSerializer(String.serializer()),
                value.tags
            )
            encodeStringElement(
                descriptor,
                descriptor.getElementIndex("target"),
                value.target
            )
        }
    }
}

@Serializable
@SerialName("SquareDependency")
private data class SquareDependencyImpl(override val tags: Set<String>, override val target: String) : SquareDependency
