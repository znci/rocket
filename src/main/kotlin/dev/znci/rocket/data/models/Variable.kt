/**
 * Copyright 2025 znci
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

package dev.znci.rocket.data.models

import dev.znci.rocket.interfaces.Storable
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.Table


@Table(name = "variables")
data class Variable( // h2 has like a million reserved keywords. that is why these are named funky.
    @Id @Column(name = "variableKey") val variableKey: String,
    @Column(name = "variableValue") val variableValue: String,
    @Column(name = "variableType") val variableType: String,
) {
    companion object {
        fun fromStorable(storable: Storable, variableKey: String): Variable {
            return Variable(
                variableKey,
                storable.toJson(),
                storable.javaClass.name,
            )
        }

        fun toStorable(variable: Variable): Storable {
            return Storable.fromJson(variable.variableType, variable.variableValue)
        }
    }
}


