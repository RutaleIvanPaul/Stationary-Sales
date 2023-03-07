package io.ramani.ramaniStationary.app.home.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signin_sheet.*
import org.kodein.di.generic.factory
import java.text.NumberFormat
import java.util.*

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModelProvider: (Fragment) -> HomeViewModel by factory()
    private lateinit var viewModel: HomeViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: HomeFlow

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.start()
    }

    private fun subscribeResponse() {
        viewModel.dailySalesStatsActionLiveData.observe(this) {
            home_total_sales_tv.text = String.format("TSH %s", getFormattedAmount(it.first().totalSales))
            home_total_customers_tv.text = getFormattedAmount(it.first().totalNumberOfCustomers)
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = HomeFlowController(baseActivity!!, R.id.main_fragment_container)

    }

    private fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)

}