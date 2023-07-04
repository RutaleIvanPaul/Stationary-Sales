package io.ramani.ramaniStationary.domain.credit

import io.ramani.ramaniStationary.data.credit.models.request.UpdateOrderPaymentStatusRequestModel
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.entities.BaseResult
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.reactivex.Single

interface CreditDataSource {

    fun getListLocations(invalidateCache: Boolean, companyId: String, page: Int): Single<PagedList<LocationModel>>
    fun updateOrderPaymentStatus(request: UpdateOrderPaymentStatusRequestModel): Single<BaseResult>

}