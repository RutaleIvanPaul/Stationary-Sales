package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createmerchant.models.response.TopPerformersRemoteModel
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.reactivex.Single

class CreateMerchantRemoteDataSource(
    private val createMerchantApi: CreateMerchantApi,
    private val topPerformersRemoteMapper: ModelMapper<TopPerformersRemoteModel, TopPerformersModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : CreateMerchantDataSource, BaseRemoteDataSource() {

    override fun getTopPerformers(companyId: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel> =
        callSingle(
            createMerchantApi.getTopPerformers(companyId, startDate, endDate, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(topPerformersRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

}