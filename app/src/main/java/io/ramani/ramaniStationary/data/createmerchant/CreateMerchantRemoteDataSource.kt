package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.data.createmerchant.models.response.TopPerformersRemoteModel
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.data.home.models.response.MerchantRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.reactivex.Maybe
import io.reactivex.Single

class CreateMerchantRemoteDataSource(
    private val createMerchantApi: CreateMerchantApi,
    private val topPerformersRemoteMapper: ModelMapper<TopPerformersRemoteModel, TopPerformersModel>,
    private val merchantRemoteMapper: ModelMapper<MerchantRemoteModel, MerchantModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : CreateMerchantDataSource, BaseRemoteDataSource() {

    override fun getTopPerformers(companyId: String, salesPersonUID: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel> =
        callSingle(
            createMerchantApi.getTopPerformers(companyId, salesPersonUID, startDate, endDate, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(topPerformersRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

    override fun registerMerchant(merchant: RegisterMerchantRequestModel): Single<MerchantModel> =
        callSingle(
            createMerchantApi.registerMerchant(merchant).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(merchantRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

    override fun saveMerchant(merchant: MerchantModel): Maybe<Long> {
        TODO("Not yet implemented")
    }

}