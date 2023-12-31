package io.ramani.ramaniStationary.data.credit

import io.ramani.ramaniStationary.data.credit.models.request.UpdateOrderPaymentStatusRequestModel
import io.ramani.ramaniStationary.domain.credit.CreditDataSource
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.entities.BaseResult
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.reactivex.Single

class CreditRepository(
    private val remoteCreditDataSource: CreditDataSource,
    private val localCreditDataSource: CreditDataSource
): CreditDataSource {

    override fun getListLocations(invalidateCache: Boolean, companyId: String, page: Int): Single<PagedList<LocationModel>>  =
        remoteCreditDataSource.getListLocations(invalidateCache, companyId, page)

    override fun updateOrderPaymentStatus(request: UpdateOrderPaymentStatusRequestModel): Single<BaseResult> =
        remoteCreditDataSource.updateOrderPaymentStatus(request)

}