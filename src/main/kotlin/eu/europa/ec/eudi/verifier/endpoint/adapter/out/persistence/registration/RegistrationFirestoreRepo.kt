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

import com.google.cloud.firestore.Firestore
import eu.europa.ec.eudi.verifier.endpoint.port.input.persistence.registration.UpdateRegistrationTransactionStatus
import eu.europa.ec.eudi.verifier.endpoint.port.out.persistence.registration.FindRegistrationByTransactionId
import eu.europa.ec.eudi.verifier.endpoint.port.out.persistence.registration.RegistrationRepo
import eu.europa.ec.eudi.verifier.endpoint.port.out.persistence.registration.StoreRegistration

class RegistrationFirestoreRepo(
    private val db: Firestore,
    private val collectionName: String,
) : RegistrationRepo {
    override val findRegistrationByTransactionId: FindRegistrationByTransactionId by lazy {
        FindRegistrationByTransactionId { transactionId ->
            val docRef = db.collection(collectionName).document(transactionId.value)
            docRef.get().get().data
                ?: error("Transaction $collectionName:$transactionId not found")
        }
    }
    override val storeRegistration: StoreRegistration by lazy {
        StoreRegistration { registrationData, transactionId ->
            val docRef = db.collection(collectionName).document(transactionId.value)

            val data =
                mapOf(
                    "readerCountry" to registrationData.readerCountry,
                    "readerCompanyName" to
                        registrationData.readerCompanyName,
                    "holderTesterInitials" to
                        registrationData.holderTesterInitials,
                    "holderDevice" to registrationData.holderDevice,
                    "dataset" to registrationData.dataset,
                    "testScenario" to registrationData.testScenario,
                )
                    .filterValues { it != null }

            docRef.set(data).get()
            data.mapValues { it.value?.toString() }
        }
    }
    override val updateRegistrationTransactionStatus:
        UpdateRegistrationTransactionStatus by lazy {
            UpdateRegistrationTransactionStatus { status, transactionId ->
                val docRef = db.collection(collectionName).document(transactionId.value)

                val data = mapOf("status" to status)

                docRef.update(data).get()
                docRef.get().get().data
                    ?: error("Transaction $collectionName:$transactionId not found")
            }
        }
}
