package io.ramani.ramaniStationary.domain.entities

/**
 * Created by Amr on 11/22/17.
 */
data class ValidationErrors(val errors: List<ValidationErrorRemote> = emptyList())