package io.ramani.ramaniStationary.data.auth.models

import io.ramani.ramaniStationary.domain.base.v2.Params

data class LoginRequestModel(val phoneNumber: String, val password: String) : Params