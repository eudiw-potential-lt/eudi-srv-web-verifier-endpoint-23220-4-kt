/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalSerializationApi::class)

package eu.europa.ec.eudi.verifier.endpoint.port.input.registration

import eu.europa.ec.eudi.verifier.endpoint.domain.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationTO(
    @SerialName("readerCountry") val readerCountry: String? = null,
    @SerialName("readerCompanyName") val readerCompanyName: String? = null,
    @SerialName("holderTesterInitials") val holderTesterInitials: String? = null,
    @SerialName("holderDevice") val holderDevice: String? = null,
    @SerialName("dataset") val dataset: String? = null,
    @SerialName("testScenario") val testScenario: String? = null,
)
