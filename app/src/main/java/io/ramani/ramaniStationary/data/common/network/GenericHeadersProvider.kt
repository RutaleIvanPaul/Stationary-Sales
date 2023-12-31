package io.ramani.ramaniStationary.data.common.network

import io.ramani.ramaniStationary.domainCore.prefs.Prefs

/**
 * Created by Amr on 9/11/17.
 */
class GenericHeadersProvider(private val prefsManager: Prefs) : HeadersProvider {

    companion object {
        const val HEADER_AUTH = "Authorization"
        const val HEADER_AUTH_TOKEN = "sessionToken"
        const val HEADER_LANGUAGE = "Accept-Language"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_ACCEPT = "Accept"
        const val HEADER_CONTENT_TYPE_VALUE = "application/json"
        const val HEADER_CLIENT = "client"
        const val HEADER_ACCOUNT_TYPE = "cType"
        const val HEADER_TIMEZONE = "tz"

    }

    override fun getHeaders(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (prefsManager.hasAccessToken) {
            map[HEADER_AUTH] = "Bearer " + prefsManager.accessToken
            map[HEADER_AUTH_TOKEN] = "Bearer " + prefsManager.accessToken
        } else {
            map[HEADER_AUTH] = "Bearer "
            map[HEADER_AUTH_TOKEN] = "Bearer "
        }


//        map[HEADER_LANGUAGE] = prefsManager.language.lowercase(Locale.getDefault())
        map[HEADER_CONTENT_TYPE] = HEADER_CONTENT_TYPE_VALUE
        map[HEADER_ACCEPT] = HEADER_CONTENT_TYPE_VALUE
        map[HEADER_CLIENT] = ApiConstants.CLIENT_HEADER
        map[HEADER_ACCOUNT_TYPE] = prefsManager.accountType
        map[HEADER_TIMEZONE] = prefsManager.timeZone
        return map
    }

    override fun getHeader(name: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}