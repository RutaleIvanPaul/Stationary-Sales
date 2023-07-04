package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.common.network.ApiConstants
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.data.home.models.response.*
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.*
import io.reactivex.Single

class HomeRemoteDataSource(
    private val homeApi: HomeApi,
    private val userAccountDetailsRemoteMapper: ModelMapper<UserAccountDetailsRemoteModel, UserAccountDetailsModel>,
    private val taxInformationRemoteMapper: ModelMapper<TaxInformationRemoteModel, TaxInformationModel>,
    private val dailySalesStatsRemoteMapper: ModelMapper<DailySalesStatsRemoteModel, DailySalesStatsModel>,
    private val taxRemoteMapper: ModelMapper<TaxRemoteModel, TaxModel>,
    private val productRemoteMapper: ModelMapper<ProductRemoteModel, ProductModel>,
    private val merchantRemoteMapper: ModelMapper<MerchantRemoteModel, MerchantModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(companyId: String, salesPersonUID: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>> =
        callSingle(
            homeApi.getDailyStats(
                companyId,
                salesPersonUID,
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

    override fun getTaxes(fromRemote: Boolean, companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>> =
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

    override fun getProducts(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>> =
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

    override fun getMerchants(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>> =
        callSingle(
            homeApi.getMerchants(
                companyId,
                startDate,
                endDate,
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

    override fun getTaxInformationByUserId(userId: String): Single<TaxInformationModel> =
        callSingle(
            homeApi.getTaxInformationByUserId(userId).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(taxInformationRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

    override fun getAccountDetails(companyId: String): Single<List<UserAccountDetailsModel>> =
        callSingle(
            homeApi.getAccountDetails(companyId).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(userAccountDetailsRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

    override fun saveMerchants(merchants: PagedList<MerchantModel>): List<Long> = listOf()
    override fun saveProducts(products: PagedList<ProductModel>): List<Long> = listOf()
    override fun saveTaxes(taxes: PagedList<TaxModel>): List<Long> = listOf()
    override fun deleteAll() {}
}