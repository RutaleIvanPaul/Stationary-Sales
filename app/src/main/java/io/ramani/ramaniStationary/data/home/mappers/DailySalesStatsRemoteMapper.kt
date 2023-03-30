package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.DailySalesStatsRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel

class DailySalesStatsRemoteMapper : ModelMapper<DailySalesStatsRemoteModel, DailySalesStatsModel> {
    override fun mapFrom(from: DailySalesStatsRemoteModel): DailySalesStatsModel =
        DailySalesStatsModel.Builder()
            .companyName(from.companyName ?: "")
            .date(from.date ?: "")
            .totalSales(from.totalSales ?: 0.0)
            .totalOrders(from.totalOrders ?: 0)
            .totalCanceledOrders(from.totalCanceledOrders ?: 0)
            .totalNumberOfCustomers(from.totalNumberOfCustomers ?: 0)
            .build()

    override fun mapTo(to: DailySalesStatsModel): DailySalesStatsRemoteModel =
        DailySalesStatsRemoteModel(
            to.companyName,
            to.date,
            to.totalSales,
            to.totalOrders,
            to.totalCanceledOrders,
            to.totalNumberOfCustomers
        )
}