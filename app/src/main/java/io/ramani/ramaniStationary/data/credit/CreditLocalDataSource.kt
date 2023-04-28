package io.ramani.ramaniStationary.data.credit

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.credit.models.request.UpdateOrderPaymentStatusRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.credit.CreditDataSource
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.entities.BaseResult
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class CreditLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : CreditDataSource, BaseRemoteDataSource() {

    override fun getListLocations(invalidateCache: Boolean, companyId: String, page: Int): Single<PagedList<LocationModel>> {
        TODO("Not yet implemented")
    }

    override fun updateOrderPaymentStatus(request: UpdateOrderPaymentStatusRequestModel): Single<BaseResult> {
        TODO("Not yet implemented")
    }

}