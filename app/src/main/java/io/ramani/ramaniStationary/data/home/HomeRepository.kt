package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.*
import io.reactivex.Single

class HomeRepository(
    private val remoteAuthDataSource: HomeDataSource,
    private val localAuthDataSource: HomeDataSource
): HomeDataSource {

    override fun getDailySalesStats(companyId: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>>  =
        remoteAuthDataSource.getDailySalesStats(companyId, page, startDate, endDate)

    override fun getTaxes(fromRemote: Boolean, companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>>  =
        if (fromRemote) {
            remoteAuthDataSource.getTaxes(
                true,
                companyId,
                userId,
                date,
                page
            ).flatMap {
                val returns = saveTaxes(taxes = it)
                return@flatMap Single.just(it)
            }
        } else {
            localAuthDataSource.getTaxes(false, companyId, userId, date, page)
        }

    override fun getProducts(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>>  =
        if (fromRemote) {
            remoteAuthDataSource.getProducts(
                true,
                companyId,
                startDate,
                endDate,
                archived,
                page
            ).flatMap {
                val returns = saveProducts(products = it)
                return@flatMap Single.just(it)
            }
        } else {
            localAuthDataSource.getProducts(false, companyId, startDate, endDate, archived, page)
        }

    override fun getMerchants(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>> =
        if (fromRemote) {
            remoteAuthDataSource.getMerchants(
                true,
                companyId,
                startDate,
                endDate,
                isActive,
                page
            ).flatMap {
                val returns = saveMerchants(merchants = it)
                return@flatMap Single.just(it)
            }
        } else {
            localAuthDataSource.getMerchants(false, companyId, startDate, endDate, isActive, page)
        }

    override fun getTaxInformationByUserId(userId: String): Single<TaxInformationModel> =
        remoteAuthDataSource.getTaxInformationByUserId(userId)

    override fun getAccountDetails(companyId: String): Single<List<UserAccountDetailsModel>> =
        remoteAuthDataSource.getAccountDetails(companyId)

    override fun saveMerchants(merchants: PagedList<MerchantModel>) = localAuthDataSource.saveMerchants(merchants)
    override fun saveProducts(products: PagedList<ProductModel>) = localAuthDataSource.saveProducts(products)
    override fun saveTaxes(taxes: PagedList<TaxModel>) = localAuthDataSource.saveTaxes(taxes)

    override fun deleteAll() = localAuthDataSource.deleteAll()

}