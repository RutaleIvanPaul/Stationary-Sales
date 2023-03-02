package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.DailySalesStatsRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel

class DailySalesStatsRemoteMapper : ModelMapper<DailySalesStatsRemoteModel, DailySalesStatsModel> {
    override fun mapFrom(from: DailySalesStatsRemoteModel): DailySalesStatsModel =
        DailySalesStatsModel.Builder()
            .companyName(from.companyName)
            .date(from.date)
            .totalSales(from.totalSales)
            .totalOrders(from.totalOrders)
            .totalCanceledOrders(from.totalCanceledOrders)
            .totalNumberOfCustomers(from.totalNumberOfCustomers)
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