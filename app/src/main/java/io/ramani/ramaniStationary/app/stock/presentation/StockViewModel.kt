package io.ramani.ramaniStationary.app.stock.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.stock.models.request.AvailableStockRequestModel
import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.ramani.ramaniStationary.data.stock.models.response.ProductsItem
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class StockViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val availableStockUseCase: BaseSingleUseCase<GetRollingStock?, AvailableStockRequestModel>
) : BaseViewModel(application, stringProvider, sessionManager){

    val availableStockProductsLiveData = MutableLiveData<List<ProductsItem>>()
    val avaialableProductsListOriginal = mutableListOf<ProductsItem>()


    override fun start(args: Map<String, Any?>) {
        getAvailableStock()
    }

    fun getAvailableStock(){
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy {
            val single = availableStockUseCase.getSingle(AvailableStockRequestModel(it.uuid))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                if (!it?.products?.isEmpty()!!) {
                    avaialableProductsListOriginal.addAll(it.products)
                    availableStockProductsLiveData.postValue(it.products)
                }
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val availableStockUseCase: BaseSingleUseCase<GetRollingStock?, AvailableStockRequestModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
                return StockViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                   availableStockUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}