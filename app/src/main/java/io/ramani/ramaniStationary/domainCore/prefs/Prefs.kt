package io.ramani.ramaniStationary.domainCore.prefs


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
    var taxObject:String
}