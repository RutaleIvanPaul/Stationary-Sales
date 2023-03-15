package io.ramani.ramaniStationary.data.home

import android.annotation.SuppressLint
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Maybe
import io.reactivex.Single

class HomeLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(companyId: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>> {
        TODO("Not yet implemented")
    }
    override fun getProducts(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>> =
        Single.just(
            PagedList.Builder<ProductModel>()
                .data(db.getProductDao().getProducts())
                .paginationMeta(
                    PaginationMeta(hasNext = false)
                )
                .build()
        )

    override fun getMerchants(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>> =
        Single.just(
            PagedList.Builder<MerchantModel>()
                .data(db.getMerchantDao().getMerchants())
                .paginationMeta(
                    PaginationMeta(hasNext = false)
                )
                .build()
        )

    override fun getTaxes(fromRemote: Boolean, companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>> =
        Single.just(
            PagedList.Builder<TaxModel>()
                .data(db.getTaxDao().getTaxes())
                .paginationMeta(
                    PaginationMeta(hasNext = false)
                )
                .build()
        )

    // This is local repository operation
    @SuppressLint("CheckResult")
    override fun saveMerchants(merchants: PagedList<MerchantModel>) = db.getMerchantDao().insertAll(merchants = merchants.data)

    @SuppressLint("CheckResult")
    override fun saveProducts(products: PagedList<ProductModel>) = db.getProductDao().insertAll(products = products.data)

    @SuppressLint("CheckResult")
    override fun saveTaxes(taxes: PagedList<TaxModel>) = db.getTaxDao().insertAll(taxes = taxes.data)

    override fun deleteAll() {
        db.getMerchantDao().deleteAll()
        db.getProductDao().deleteAll()
        db.getTaxDao().deleteAll()
    }
}