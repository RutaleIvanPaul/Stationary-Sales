package io.ramani.ramaniStationary.app.reports.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.reports.presentation.adapter.NameValueRVAdapter
import kotlinx.android.synthetic.main.fragment_reports.*
import org.kodein.di.generic.factory

class ReportsFragment : BaseFragment() {
    companion object {
        fun newInstance() = ReportsFragment()
    }

    private val viewModelProvider: (Fragment) -> ReportsViewModel by factory()
    private lateinit var viewModel: ReportsViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun getLayoutResId(): Int = R.layout.fragment_reports

    private lateinit var topCustomersAdapter: NameValueRVAdapter
    private lateinit var topProductsAdapter: NameValueRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        reports_print_summary.setOnSingleClickListener {

        }

        updateUI()
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
        viewModel.onTopPerformersLoadedLiveData.observe(this) {
            updateUI()
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        reports_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updateUI() {
        reports_total_sales_value.text = viewModel.totalSales

        updateRV()
    }

    private fun updateRV() {
        topCustomersAdapter = NameValueRVAdapter(viewModel.topMerchants.take(4).toMutableList())

        reports_top_customers_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = topCustomersAdapter
        }

        topProductsAdapter = NameValueRVAdapter(viewModel.topSalePeoples.take(4).toMutableList())

        reports_top_products_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = topCustomersAdapter
        }

    }
}