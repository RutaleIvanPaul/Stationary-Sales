package io.ramani.ramaniStationary.data.credit

import io.ramani.ramaniStationary.data.credit.models.response.GetLocationsRemoteModel
import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.*

interface CreditApi {

    @GET("/get/sfa/locations/v4")
    fun getLocations(
        @Header("invalidate_cache") invalidateCache: Boolean,
        @Query("sellerId") companyId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Single<BaseResponse<GetLocationsRemoteModel>>

}