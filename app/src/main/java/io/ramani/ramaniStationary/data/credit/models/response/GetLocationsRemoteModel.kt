package io.ramani.ramaniStationary.data.credit.models.response

import com.google.gson.annotations.SerializedName

data class GetLocationsRemoteModel(
    @SerializedName("docs")
    val docs: List<LocationRemoteModel>?
)
