package io.ramani.ramaniStationary.data.credit.mappers

import io.ramani.ramaniStationary.data.createmerchant.models.request.MerchantRouteModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.MetaDataItem
import io.ramani.ramaniStationary.data.credit.models.response.LocationRemoteModel
import io.ramani.ramaniStationary.data.credit.models.response.MerchantRouteRemoteModel
import io.ramani.ramaniStationary.data.credit.models.response.MetaDataItemRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.credit.model.CreditOrdersModel
import io.ramani.ramaniStationary.domain.credit.model.LocationModel

class LocationRemoteMapper(
    private val merchantRouteRemoteMapper: ModelMapper<MerchantRouteRemoteModel, MerchantRouteModel>,
    private val metaDataItemRemoteMapper: ModelMapper<MetaDataItemRemoteModel, MetaDataItem>
) : ModelMapper<LocationRemoteModel, LocationModel> {

    override fun mapFrom(from: LocationRemoteModel): LocationModel {
        val routes: ArrayList<MerchantRouteModel> = ArrayList()
        for (route in from.routes ?: listOf()) {
            routes.add(merchantRouteRemoteMapper.mapFrom(route))
        }

        val metaItems: ArrayList<MetaDataItem> = ArrayList()
        for (metaItem in from.metaData ?: listOf()) {
            metaItems.add(metaDataItemRemoteMapper.mapFrom(metaItem))
        }

        return LocationModel.Builder()
                .name(from.name ?: "")
                .salesPersonName(from.salesPersonName ?: "")
                .salesPersonUuid(from.salesPersonUuid ?: "")
                .creditOrders(from.creditOrders ?: CreditOrdersModel())
                .maxCredit(from.maxCredit ?: 0.0)
                .memberNumber(from.memberNumber ?: "")
                .merchantId(from.merchantId ?: "")
                .merchantTIN(from.merchantTIN ?: "")
                .merchantVRN(from.merchantVRN ?: "")
                .merchantType(from.merchantType ?: "")
                .merchantStatus(from.merchantStatus ?: "")
                .routes(routes)
                .metaData(metaItems)
                .gps(from.gps ?: "")
                .isActive(from.isActive ?: false)
                .build()
    }

    override fun mapTo(to: LocationModel): LocationRemoteModel {
        val routes: ArrayList<MerchantRouteRemoteModel> = ArrayList()
        for (route in to.routes) {
            routes.add(merchantRouteRemoteMapper.mapTo(route))
        }

        val metaItems: ArrayList<MetaDataItemRemoteModel> = ArrayList()
        for (metaItem in to.metaData) {
            metaItems.add(metaDataItemRemoteMapper.mapTo(metaItem))
        }

        return LocationRemoteModel(
                to.name,
                to.salesPersonName,
                to.salesPersonUuid,
                to.creditOrders,
                to.maxCredit,
                to.memberNumber,
                to.merchantId,
                to.merchantTIN,
                to.merchantVRN,
                to.merchantType,
                to.merchantStatus,
                routes,
                metaItems,
                to.gps,
                to.isActive
            )
    }
}