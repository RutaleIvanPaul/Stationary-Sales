package io.ramani.ramaniStationary.data.createmerchant.mappers

import io.ramani.ramaniStationary.data.createmerchant.models.response.NameValueRemoteModel
import io.ramani.ramaniStationary.data.createmerchant.models.response.TopPerformersRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel

class RegisterMerchantRemoteMapper(
    private val nameValueRemoteMapper: ModelMapper<NameValueRemoteModel, NameValueModel>,
) : ModelMapper<TopPerformersRemoteModel, TopPerformersModel> {

    override fun mapFrom(from: TopPerformersRemoteModel): TopPerformersModel {
        val topSalesPeople: ArrayList<NameValueModel> = ArrayList()
        from.topSalespeople?.forEach {
            topSalesPeople.add(nameValueRemoteMapper.mapFrom(it))
        }

        val topMerchants: ArrayList<NameValueModel> = ArrayList()
        from.topMerchants?.forEach {
            topMerchants.add(nameValueRemoteMapper.mapFrom(it))
        }

        return TopPerformersModel.Builder()
                .topSalesPeople(topSalesPeople)
                .topMerchants(topMerchants)
                .build()
    }

    override fun mapTo(to: TopPerformersModel): TopPerformersRemoteModel {
        val topSalesPeople: ArrayList<NameValueRemoteModel> = ArrayList()
        to.topSalesPeople.forEach {
            topSalesPeople.add(nameValueRemoteMapper.mapTo(it))
        }

        val topMerchants: ArrayList<NameValueRemoteModel> = ArrayList()
        to.topMerchants.forEach {
            topMerchants.add(nameValueRemoteMapper.mapTo(it))
        }

        return TopPerformersRemoteModel(
                topSalesPeople,
                topMerchants
            )
    }
}