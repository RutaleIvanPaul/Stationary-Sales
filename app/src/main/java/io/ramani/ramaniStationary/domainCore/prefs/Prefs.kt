package io.ramani.ramaniStationary.domainCore.prefs

import io.ramani.ramaniStationary.domain.home.model.TaxInformationModel
import io.ramani.ramaniStationary.domain.home.model.UserAccountDetailsModel


/**
 * Created by Amr on 10/5/17.
 */
interface Prefs {
    var currentUser: String
    var accessToken: String
    val hasAccessToken: Boolean
    var refreshToken: String
    var currentWarehouse: String
    var accountType: String
    var timeZone: String
    var lastSyncTime: String
    var currency:String
    var userAccountDetails: UserAccountDetailsModel
    var taxInformation: TaxInformationModel
}