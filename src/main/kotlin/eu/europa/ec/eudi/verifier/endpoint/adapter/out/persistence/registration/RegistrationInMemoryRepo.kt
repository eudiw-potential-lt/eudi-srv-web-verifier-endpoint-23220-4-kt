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
package eu.europa.ec.eudi.verifier.endpoint.adapter.out.persistence.registration

import eu.europa.ec.eudi.verifier.endpoint.domain.TransactionId
import eu.europa.ec.eudi.verifier.endpoint.port.input.persistence.registration.UpdateRegistrationTransactionStatus
import eu.europa.ec.eudi.verifier.endpoint.port.out.persistence.registration.FindRegistrationByTransactionId
import eu.europa.ec.eudi.verifier.endpoint.port.out.persistence.registration.RegistrationRepo
import eu.europa.ec.eudi.verifier.endpoint.port.out.persistence.registration.StoreRegistration
import java.util.concurrent.ConcurrentHashMap

class RegistrationInMemoryRepo : RegistrationRepo {

    private val registrations = ConcurrentHashMap<TransactionId, MutableMap<String, Any?>>()

    override val findRegistrationByTransactionId: FindRegistrationByTransactionId by lazy {
        FindRegistrationByTransactionId { transactionId ->
            registrations[transactionId]?.toMap()?.filterValues { it != null }?.mapValues {
                it.value!!
            }
                ?: emptyMap()
        }
    }
    override val storeRegistration: StoreRegistration by lazy {
        StoreRegistration { registrationData, transactionId ->
            val data =
                mutableMapOf<String, Any?>(
                    "readerCountry" to registrationData.readerCountry,
                    "readerCompanyName" to registrationData.readerCompanyName,
                    "holderTesterInitials" to registrationData.holderTesterInitials,
                    "holderDevice" to registrationData.holderDevice,
                    "dataset" to registrationData.dataset,
                    "testScenario" to registrationData.testScenario,
                )
                    .filterValues { it != null }

            registrations[transactionId] = data.toMutableMap()
            data.mapValues { it.value?.toString() }
        }
    }
    override val updateRegistrationTransactionStatus: UpdateRegistrationTransactionStatus by lazy {
        UpdateRegistrationTransactionStatus { status, transactionId ->
            registrations
                .computeIfPresent(transactionId) { _, data ->
                    data["status"] = status
                    data
                }
                ?.toMap()
                ?.filterValues { it != null }
                ?.mapValues { it.value!! }
                ?: emptyMap()
        }
    }
}
