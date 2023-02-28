package io.ramani.ramaniStationary.domain.entities

import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class BaseResult(val message: String) {
    class Builder : IBuilder<BaseResult> {
        private var message = ""
        fun message(message: String): Builder {
            this.message = message
            return this
        }

        override fun build(): BaseResult =
            BaseResult(message)
    }
}