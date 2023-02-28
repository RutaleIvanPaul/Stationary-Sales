package io.ramani.ramaniStationary.data.common.source.remote.mappers

import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.entities.PaginationMeta

class PaginationMetaRemoteModelMapper : ModelMapper<PaginationMetaRemote, PaginationMeta> {
    override fun mapFrom(from: PaginationMetaRemote): PaginationMeta =

        PaginationMeta.Builder()
            .total(from.total ?: 0)
            .currenPage(from.currentPage ?: 0)
            .perPage(from.perPage ?: 0)
            .totalPages(from.totalPages ?: 0)
            .nextCursor(from.nextCursor ?: "")
            .hasNext(from.hasNext ?: false)
            .build()

    override fun mapTo(to: PaginationMeta): PaginationMetaRemote =
        PaginationMetaRemote(
            to.total,
            to.perPage,
            to.currentPage,
            to.nextCursor,
            to.hasNext,
            to.totalPages
        )
}