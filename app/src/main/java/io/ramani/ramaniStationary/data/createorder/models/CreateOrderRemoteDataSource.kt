package io.ramani.ramaniStationary.data.createorder.models

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.data.createorder.models.response.AvailableStockRemoteModel
import io.ramani.ramaniStationary.data.createorder.models.response.SaleRemoteModel
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.reactivex.Single

class CreateOrderRemoteDataSource(
    private val createOrderApi: CreateOrderApi,
    private val availableStockRemoteMapper: ModelMapper<AvailableStockRemoteModel, AvailableStockModel>,
    private val saleRemoteMapper: ModelMapper<SaleRemoteModel, SaleModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : CreateOrderDataSource, BaseRemoteDataSource() {

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>> =
        callSingle(
            createOrderApi.getAvailableStock("true", salesPersonUID).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(availableStockRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

    override fun postNewSale(sale: SaleRequestModel): Single<SaleModel> =
        callSingle(
            createOrderApi.postNewSale("true", sale).flatMap {
                // [RI-1541][Adrian] Based on discussing with the backend team, they will queue the request and they will return response as null
                if (it.status == 200L) {
                    Single.just(SaleModel())
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

}