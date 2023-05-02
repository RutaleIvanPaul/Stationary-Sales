package io.ramani.ramaniStationary.data.common.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import io.ramani.ramaniStationary.app.createorder.presentation.CREATE_ORDER_MODEL
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.domain.home.model.TaxInformationModel
import io.ramani.ramaniStationary.domain.home.model.UserAccountDetailsModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs

/**
 * Created by Amr on 9/8/17.
 */
open class PrefsManager(context: Context) : Prefs {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        PrefsConstants.PREF_FILE_NAME,
        Context.MODE_PRIVATE
    )

    override var currentUser: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_CURRENT_USER, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENT_USER, value).apply()
        }
    override var accessToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_ACCESS_TOKEN, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_ACCESS_TOKEN, value).apply()
        }


    override val hasAccessToken: Boolean
        get() = contains(PrefsConstants.PREF_ACCESS_TOKEN)

    override var refreshToken: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_REFRESH_TOKEN, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_REFRESH_TOKEN, value).apply()
        }

    override var currentWarehouse: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_CURRENT_WAREHOUSE, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENT_WAREHOUSE, value).apply()
        }
//    override var currentProducts: List<ProductsUIModel>?
//        get() =  sharedPrefs.getList(PrefsConstants.PREF_CURRENT_PRODUCTS, null) ?: null
//
//        set(value) {}

    override var accountType: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_ACCOUNT_TYPE, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_ACCOUNT_TYPE, value).apply()
        }

    override var timeZone: String
        get() = sharedPrefs.getString(PrefsConstants.PREF_TIMEZONE, null) ?: ""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_TIMEZONE, value).apply()
        }

    override var lastSyncTime: String
        get() {
            val value = sharedPrefs.getString(PrefsConstants.PREF_LAST_SYNC_TIME, "") ?: ""
            return value.ifEmpty { "1970-01-01T00:00:00" }
        }
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_LAST_SYNC_TIME, value).apply()
        }

    override var currency: String
        get() =
            sharedPrefs.getString(PrefsConstants.PREF_CURRENCY,null)?:""
        set(value) {
            sharedPrefs.edit().putString(PrefsConstants.PREF_CURRENCY, value).apply()
        }

    override var userAccountDetails: UserAccountDetailsModel
        get() {
            val userDetailsString = sharedPrefs.getString(PrefsConstants.PREF_USER_ACCOUNT_DETAILS, null) ?: ""
            return if (userDetailsString.isNotEmpty()) Gson().fromJson(userDetailsString, UserAccountDetailsModel::class.java) else UserAccountDetailsModel()
        }
        set(value) {
            val userDetailsString = value.toJson()
            sharedPrefs.edit().putString(PrefsConstants.PREF_USER_ACCOUNT_DETAILS, userDetailsString).apply()
        }

    override var taxInformation: TaxInformationModel
        get() {
            val taxInformationString = sharedPrefs.getString(PrefsConstants.PREF_TAX_INFORMATION, null) ?: ""
            return if (taxInformationString.isNotEmpty()) Gson().fromJson(taxInformationString, TaxInformationModel::class.java) else TaxInformationModel()
        }
        set(value) {
            val taxInformationString = value.toJson()
            sharedPrefs.edit().putString(PrefsConstants.PREF_TAX_INFORMATION, taxInformationString).apply()
        }

    private fun contains(key: String) = sharedPrefs.contains(key)

    private fun containsAndHasValue(key: String) =
        sharedPrefs.getString(key, "")?.isNotEmpty() == true
}