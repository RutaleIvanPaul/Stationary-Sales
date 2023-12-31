package io.ramani.ramaniStationary.data.home

import android.annotation.SuppressLint
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.*
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class HomeLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(companyId: String, salesPersonUID: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>> {
        TODO("Not yet implemented")
    }
    override fun getProducts(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>> =
        db.getProductDao().getProducts().flatMap {
            Single.just(
                PagedList.Builder<ProductModel>()
                    .data(it)
                    .paginationMeta(
                        PaginationMeta(hasNext = false)
                    )
                    .build()
            )
        }

    override fun getMerchants(fromRemote: Boolean, companyId: String, startDate: String, endDate: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>> =
        db.getMerchantDao().getMerchants().flatMap {
             Single.just(
                PagedList.Builder<MerchantModel>()
                    .data(it)
                    .paginationMeta(
                        PaginationMeta(hasNext = false)
                    )
                    .build()
            )
        }

    override fun getTaxes(fromRemote: Boolean, companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>> =
        db.getTaxDao().getTaxes().flatMap {
            Single.just(
                PagedList.Builder<TaxModel>()
                    .data(it)
                    .paginationMeta(
                        PaginationMeta(hasNext = false)
                    )
                    .build()
            )
        }

    override fun getTaxInformationByUserId(userId: String): Single<TaxInformationModel> {
        TODO("Not yet implemented")
    }

    override fun getAccountDetails(companyId: String): Single<List<UserAccountDetailsModel>> {
        TODO("Not yet implemented")
    }

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