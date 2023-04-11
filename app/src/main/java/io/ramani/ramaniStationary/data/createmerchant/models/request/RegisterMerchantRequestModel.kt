package io.ramani.ramaniStationary.data.createmerchant.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel

data class RegisterMerchantRequestModel(
    val name: String,
    val gps: String,
    val sellerId: String,
    val salesPersonUID: String,
    val salesPersonName: String,
    val merchantStatus: Int,
    val categories: Int,
    val merchantType: Int,
    val merchantTIN: String,
    val merchantVRN: String,
    var members: List<MerchantMemberModel> = listOf(),
    val isActive: Boolean = false,
    val routes: List<MerchantRouteModel> = mutableListOf(MerchantRouteModel("WalkIn", "WalkIn")),
    val metaData: List<MetaDataItem> = mutableListOf(MetaDataItem("WalkIn", "WalkIn", "WalkIn")),
): Params

data class MerchantRouteModel(
    val routeId: String,
    val routeName: String
): Params

data class MetaDataItem(
    val id: String,
    val value: String,
    val fieldId: String
): Params