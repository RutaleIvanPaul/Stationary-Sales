package io.ramani.ramaniStationary.data.credit

import io.ramani.ramaniStationary.data.common.network.ApiConstants
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.credit.models.request.UpdateOrderPaymentStatusRequestModel
import io.ramani.ramaniStationary.data.credit.models.response.LocationRemoteModel
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.credit.CreditDataSource
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.entities.BaseResult
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.reactivex.Single

class CreditRemoteDataSource(
    private val creditApi: CreditApi,
    private val locationRemoteMapper: ModelMapper<LocationRemoteModel, LocationModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : CreditDataSource, BaseRemoteDataSource() {

    override fun getListLocations(invalidateCache: Boolean, companyId: String, page: Int): Single<PagedList<LocationModel>> =
        callSingle(
            creditApi.getLocations(
                invalidateCache,
                companyId,
                page,
                ApiConstants.PAGINATION_PER_PAGE_SIZE
            ).flatMap {
                val data = it.data
                val meta = it.meta
                Single.just(
                    PagedList.Builder<LocationModel>()
                        .data(data?.docs?.mapFromWith(locationRemoteMapper) ?: listOf())
                        .paginationMeta(
                            meta?.
                            mapFromWith(metaRemoteMapper)?:
                            PaginationMeta()
                        )
                        .build()
                )
            }
        )

    override fun updateOrderPaymentStatus(request: UpdateOrderPaymentStatusRequestModel): Single<BaseResult> =
        callSingle(
            creditApi.updateOrderPaymentStatus(request).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data)
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

}