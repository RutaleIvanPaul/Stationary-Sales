package io.ramani.ramaniStationary.app.reports.presentation

import android.annotation.SuppressLint
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
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
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

        updateUI(viewModel.salesSummary)
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
            updateTopPerformers()
        }

        viewModel.onSalesSummaryLoadedLiveData.observe(this) {
            updateUI(it)
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

    @SuppressLint("SetTextI18n")
    private fun updateUI(summary:SalesSummaryStatisticsModel) {
        val currency = summary.currency

        reports_total_sales_value_currency.text = currency
        reports_total_sales_value.text = viewModel.getFormattedAmount(summary.totalSalesValue.value)

        reports_total_sales_count.text = viewModel.getFormattedAmount(summary.totalSalesCount.value)

        reports_total_credit_given_currency.text = currency
        reports_total_credit_given.text = viewModel.getFormattedAmount(summary.totalCreditGiven.value)
    }

    private fun updateTopPerformers() {
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