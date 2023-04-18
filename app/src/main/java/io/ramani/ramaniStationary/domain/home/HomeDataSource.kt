package io.ramani.ramaniStationary.domain.home

import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.*
import io.reactivex.Single

interface HomeDataSource {
    fun getDailySalesStats(companyId: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>>
    fun getTaxes(fromRemote: Boolean, companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>>
    fun getProducts(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>>
    fun getMerchants(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>>

    fun getTaxInformationByUserId(userId: String): Single<TaxInformationModel>
    fun getAccountDetails(companyId: String): Single<List<UserAccountDetailsModel>>

    fun saveMerchants(merchants: PagedList<MerchantModel>): List<Long>
    fun saveProducts(products: PagedList<ProductModel>): List<Long>
    fun saveTaxes(taxes: PagedList<TaxModel>): List<Long>

    fun deleteAll()

}