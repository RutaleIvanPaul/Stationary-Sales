package io.ramani.ramaniStationary.app.main.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider

/**
 * Created by Amr on 9/18/17.
 */
class MainViewModelFactory(
    private val application: Application,
    private val stringProvider: IStringProvider,
    private val sessionManager: ISessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application, stringProvider, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}