package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.common.network.ApiConstants
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.data.home.models.response.DailySalesStatsRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.MerchantRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.ProductRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.TaxRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.reactivex.Single

class HomeRemoteDataSource(
    private val homeApi: HomeApi,
    private val dailySalesStatsRemoteMapper: ModelMapper<DailySalesStatsRemoteModel, DailySalesStatsModel>,
    private val taxRemoteMapper: ModelMapper<TaxRemoteModel, TaxModel>,
    private val productRemoteMapper: ModelMapper<ProductRemoteModel, ProductModel>,
    private val merchantRemoteMapper: ModelMapper<MerchantRemoteModel, MerchantModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(companyId: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>> =
        callSingle(
            homeApi.getDailyStats(
                companyId,
                page,
                ApiConstants.PAGINATION_PER_PAGE_SIZE,
                startDate,
                endDate
            ).flatMap {
                val data = it.data
                val meta = it.meta
                Single.just(
                    PagedList.Builder<DailySalesStatsModel>()
                        .data(data?.mapFromWith(dailySalesStatsRemoteMapper) ?: listOf())
                        .paginationMeta(
                            meta?.
                            mapFromWith(metaRemoteMapper)?:
                            PaginationMeta()
                        )
                        .build()
                )
            }
        )

    override fun getTaxes(companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>> =
        callSingle(
            homeApi.getTaxes(
                date,
                companyId,
                userId,
                page,
                ApiConstants.PAGINATION_PER_PAGE_SIZE
            ).flatMap {
                val data = it.data
                val meta = it.meta
                Single.just(
                    PagedList.Builder<TaxModel>()
                        .data(data?.mapFromWith(taxRemoteMapper) ?: listOf())
                        .paginationMeta(
                            meta?.
                            mapFromWith(metaRemoteMapper)?:
                            PaginationMeta()
                        )
                        .build()
                )
            }
        )

    override fun getProducts(companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>> =
        callSingle(
            homeApi.getProducts(
                companyId,
                startDate,
                endDate,
                archived,
                page,
                ApiConstants.PAGINATION_PER_PAGE_SIZE
            ).flatMap {
                val data = it.data
                val meta = it.meta
                Single.just(
                    PagedList.Builder<ProductModel>()
                        .data(data?.mapFromWith(productRemoteMapper) ?: listOf())
                        .paginationMeta(
                            meta?.
                            mapFromWith(metaRemoteMapper)?:
                            PaginationMeta()
                        )
                        .build()
                )
            }
        )

    override fun getMerchants(date: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>> =
        callSingle(
            homeApi.getMerchants(
                date,
                isActive,
                page,
                ApiConstants.PAGINATION_PER_PAGE_SIZE
            ).flatMap {
                val data = it.data
                val meta = it.meta
                Single.just(
                    PagedList.Builder<MerchantModel>()
                        .data(data?.mapFromWith(merchantRemoteMapper) ?: listOf())
                        .paginationMeta(
                            meta?.
                            mapFromWith(metaRemoteMapper)?:
                            PaginationMeta()
                        )
                        .build()
                )
            }
        )
}