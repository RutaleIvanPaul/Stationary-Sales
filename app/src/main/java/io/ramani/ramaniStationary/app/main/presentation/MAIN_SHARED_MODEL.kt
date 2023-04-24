package io.ramani.ramaniStationary.app.main.presentation

import io.ramani.ramaniStationary.domain.base.SingleLiveEvent

class MAIN_SHARED_MODEL {

    companion object {
        var isOnline = true
        val onNetworkStatusChangedLiveData = SingleLiveEvent<Boolean>()     // Use whenever network connection status is changed

        var isSynching = false      // Indicate whether the operation of synchronize is being done

        fun updateNetworkStatus(isConnected: Boolean) {
            isOnline = isConnected
            onNetworkStatusChangedLiveData.postValue(isConnected)
        }
    }

}