package io.ramani.ramaniStationary.app.main.presentation

import io.ramani.ramaniStationary.domain.base.SingleLiveEvent

class MAIN_SHARED_MODEL {

    companion object {
        var isOnline = true
        val onNetworkStatusChangedLiveData = SingleLiveEvent<Boolean>()     // Use whenever network connection status is changed

        fun updateNetworkStatus(isConnected: Boolean) {
            isOnline = isConnected
            onNetworkStatusChangedLiveData.postValue(isConnected)
        }
    }

}