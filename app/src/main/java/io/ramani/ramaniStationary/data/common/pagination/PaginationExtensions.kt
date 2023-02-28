package io.ramani.ramaniStationary.data.common.pagination

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.UniModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.entities.PaginationMeta

inline fun <reified T : Any, reified V> BaseResponse<List<T>>?.buildPagedList(
    modelMapper: UniModelMapper<T, V>,
    paginationModelMapper: ModelMapper<PaginationMetaRemote,
            PaginationMeta>
)
        : PagedList<V> =
    let { response ->
        PagedList.Builder<V>().run {
            data(response?.data?.mapFromWith(modelMapper) ?: listOf())
            paginationMeta(
                response?.meta?.mapFromWith(paginationModelMapper)
                    ?: PaginationMeta()
            )
            build()
        }
    }