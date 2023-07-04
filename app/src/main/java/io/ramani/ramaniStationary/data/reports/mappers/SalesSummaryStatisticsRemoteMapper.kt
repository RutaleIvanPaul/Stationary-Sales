package io.ramani.ramaniStationary.data.reports.mappers

import io.ramani.ramaniStationary.data.reports.models.response.SalesSummaryStatisticsRemoteModel
import io.ramani.ramaniStationary.data.reports.models.response.ValuePercentageRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.ramani.ramaniStationary.domain.reports.model.ValuePercentageModel

class SalesSummaryStatisticsRemoteMapper(
    private val valuePercentageRemoteMapper: ModelMapper<ValuePercentageRemoteModel, ValuePercentageModel>,
) : ModelMapper<SalesSummaryStatisticsRemoteModel, SalesSummaryStatisticsModel> {

    override fun mapFrom(from: SalesSummaryStatisticsRemoteModel): SalesSummaryStatisticsModel =
        SalesSummaryStatisticsModel.Builder()
                .companyId(from.companyId ?: "")
                .startDate(from.startDate ?: "")
                .endDate(from.endDate ?: "")
                .currency(from.currency ?: "")
                .totalSalesValue(valuePercentageRemoteMapper.mapFrom(from.totalSalesValue ?: ValuePercentageRemoteModel(0.0, 0.0)))
                .totalSalesCount(valuePercentageRemoteMapper.mapFrom(from.totalSalesCount ?: ValuePercentageRemoteModel(0.0, 0.0)))
                .totalCreditGiven(valuePercentageRemoteMapper.mapFrom(from.totalCreditGiven ?: ValuePercentageRemoteModel(0.0, 0.0)))
                .merchantsVisited(valuePercentageRemoteMapper.mapFrom(from.merchantsVisited ?: ValuePercentageRemoteModel(0.0, 0.0)))
                .newMerchants(valuePercentageRemoteMapper.mapFrom(from.newMerchants ?: ValuePercentageRemoteModel(0.0, 0.0)))
                .averageTransactionValue(valuePercentageRemoteMapper.mapFrom(from.averageTransactionValue ?: ValuePercentageRemoteModel(0.0, 0.0)))
                .build()

    override fun mapTo(to: SalesSummaryStatisticsModel): SalesSummaryStatisticsRemoteModel =
        SalesSummaryStatisticsRemoteModel(
                to.companyId,
                to.startDate,
                to.endDate,
                to.currency,
                valuePercentageRemoteMapper.mapTo(to.totalSalesValue),
                valuePercentageRemoteMapper.mapTo(to.totalSalesCount),
                valuePercentageRemoteMapper.mapTo(to.totalCreditGiven),
                valuePercentageRemoteMapper.mapTo(to.merchantsVisited),
                valuePercentageRemoteMapper.mapTo(to.newMerchants),
                valuePercentageRemoteMapper.mapTo(to.averageTransactionValue)
            )
}