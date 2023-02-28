package io.ramani.ramaniStationary.domain.entities

import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class PaginationMeta(
    val total: Int = 0,
    val perPage: Int = 0,
    val currentPage: Int = 0,
    val nextCursor: String = "",
    val hasNext: Boolean = false,
    val totalPages: Int = 0
) {

    class Builder : IBuilder<PaginationMeta> {
        private var total = 0
        private var perPage = 0
        private var currentPage = 0
        private var totalPages = 0
        private var nextCursor = ""
        private var hasNext = false

        fun total(total: Int): Builder {
            this.total = total
            return this
        }

        fun hasNext(hasNext: Boolean): Builder {
            this.hasNext = hasNext
            return this
        }

        fun perPage(perPage: Int): Builder {
            this.perPage = perPage
            return this
        }

        fun currenPage(currentPage: Int): Builder {
            this.currentPage = currentPage
            return this
        }

        fun totalPages(totalPages: Int): Builder {
            this.totalPages = totalPages
            return this
        }

        fun nextCursor(nextCursor: String): Builder {
            this.nextCursor = nextCursor
            return this
        }

        override fun build(): PaginationMeta = PaginationMeta(
            total,
            perPage,
            currentPage,
            nextCursor,
            hasNext,
            totalPages
        )
    }
}