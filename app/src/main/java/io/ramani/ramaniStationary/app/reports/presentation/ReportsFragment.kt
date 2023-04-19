package io.ramani.ramaniStationary.app.reports.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.reports.presentation.adapter.NameValueRVAdapter
import io.ramani.ramaniStationary.app.reports.presentation.adapter.NameValueSmallRVAdapter
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import kotlinx.android.synthetic.main.fragment_reports.*
import kotlinx.android.synthetic.main.fragment_reports.reports_top_customers_list
import kotlinx.android.synthetic.main.fragment_reports.reports_top_products_list
import kotlinx.android.synthetic.main.fragment_reports.reports_total_credit_given
import kotlinx.android.synthetic.main.fragment_reports.reports_total_credit_given_currency
import kotlinx.android.synthetic.main.fragment_reports.reports_total_sales_count
import kotlinx.android.synthetic.main.fragment_reports.reports_total_sales_value
import kotlinx.android.synthetic.main.fragment_reports.reports_total_sales_value_currency
import kotlinx.android.synthetic.main.layout_reports_print.*
import org.kodein.di.generic.factory
import java.util.*

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

    private lateinit var topCustomersSmallAdapter: NameValueSmallRVAdapter
    private lateinit var topProductsSmallAdapter: NameValueSmallRVAdapter

    enum class DayRange {
        NONE, TODAY, YESTERDAY, LAST_7_DAYS, LAST_30_DAYS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        report_today_btn.setOnClickListener {
            viewModel.updateDate(DayRange.TODAY)
        }

        report_yesterday_btn.setOnClickListener {
            viewModel.updateDate(DayRange.YESTERDAY)
        }

        report_last_7_days_btn.setOnClickListener {
            viewModel.updateDate(DayRange.LAST_7_DAYS)
        }

        report_last_30_days_btn.setOnClickListener {
            viewModel.updateDate(DayRange.LAST_30_DAYS)
        }

        report_day_tv.setOnClickListener {
            // Open date picker
            val startDatePickerDialog = DatePickerDialog(requireActivity(),
                startDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                viewModel.calendar.get(Calendar.YEAR),
                viewModel.calendar.get(Calendar.MONTH),
                viewModel.calendar.get(Calendar.DAY_OF_MONTH)
            )
            startDatePickerDialog.setMessage(getString(R.string.pick_start_date))
            startDatePickerDialog.show()
        }

        reports_print_summary.setOnSingleClickListener {
            val printView = reports_print_scrollview

            val bitmap = Bitmap.createBitmap(printView.width, printView.getChildAt(0).height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val bgDrawable = printView.background
            if (bgDrawable!=null){
                bgDrawable.draw(canvas)
            }
            else{
                canvas.drawColor(Color.WHITE)
            }
            printView.draw(canvas)

            viewModel.printBitmap(bitmap)
       }

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

        viewModel.onDateChangedLiveData.observe(this) {
            report_day_tv.text = viewModel.getDateRangeString()
            updateButtons(it)
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
    private fun updateUI(summary: SalesSummaryStatisticsModel) {
        val currency = summary.currency

        reports_total_sales_value_currency.text = currency
        reports_total_sales_value.text = viewModel.getFormattedAmount(summary.totalSalesValue.value)

        reports_total_sales_count.text = viewModel.getFormattedAmount(summary.totalSalesCount.value)

        reports_total_credit_given_currency.text = currency
        reports_total_credit_given.text = viewModel.getFormattedAmount(summary.totalCreditGiven.value)

        // To support print layout
        reports_print_total_sales_value_currency.text = currency
        reports_print_total_sales_value.text = viewModel.getFormattedAmount(summary.totalSalesValue.value)

        reports_print_total_sales_count.text = viewModel.getFormattedAmount(summary.totalSalesCount.value)

        reports_print_total_credit_given_currency.text = currency
        reports_print_total_credit_given.text = viewModel.getFormattedAmount(summary.totalCreditGiven.value)
    }

    private fun updateTopPerformers() {
        topCustomersAdapter = NameValueRVAdapter(viewModel.topMerchants.take(4).toMutableList())
        reports_top_customers_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = topCustomersAdapter
        }

        topProductsAdapter = NameValueRVAdapter(viewModel.topProducts.take(4).toMutableList())
        reports_top_products_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = topProductsAdapter
        }

        // To support print layout
        topCustomersSmallAdapter = NameValueSmallRVAdapter(viewModel.topMerchants.take(4).toMutableList())
        reports_print_top_customers_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = topCustomersSmallAdapter
        }

        topProductsSmallAdapter = NameValueSmallRVAdapter(viewModel.topProducts.take(4).toMutableList())
        reports_print_top_products_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = topProductsSmallAdapter
        }
    }

    private fun updateButtons(dayRange: DayRange) {
        val startDate = viewModel.startDate
        val endDate = viewModel.endDate
        if (dayRange == DayRange.NONE) {
            viewModel.dayRange = if (DateUtils.isToday(startDate.time) && DateUtils.isToday(endDate.time)) {
                DayRange.TODAY
            } else if (viewModel.isYesterday(startDate) && viewModel.isYesterday(endDate)) {
                DayRange.YESTERDAY
            } else {
                DayRange.NONE
            }
        }

        val context = requireContext()
        val selectedDrawable = ContextCompat.getDrawable(context, R.drawable.action_button_normal)
        val noSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.action_button_grey)

        report_today_btn.background = noSelectedDrawable
        report_yesterday_btn.background = noSelectedDrawable
        report_last_7_days_btn.background = noSelectedDrawable
        report_last_30_days_btn.background = noSelectedDrawable

        when (dayRange) {
            DayRange.TODAY -> report_today_btn.background = selectedDrawable
            DayRange.YESTERDAY -> report_yesterday_btn.background = selectedDrawable
            DayRange.LAST_7_DAYS -> report_last_7_days_btn.background = selectedDrawable
            DayRange.LAST_30_DAYS -> report_last_30_days_btn.background = selectedDrawable
            else -> {}
        }
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.calendar.set(Calendar.YEAR, year)
            viewModel.calendar.set(Calendar.MONTH, monthOfYear)
            viewModel.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.startDate = viewModel.calendar.time

            val endDatePickerDialog = DatePickerDialog(requireActivity(),
                endDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                viewModel.calendar.get(Calendar.YEAR),
                viewModel.calendar.get(Calendar.MONTH),
                viewModel.calendar.get(Calendar.DAY_OF_MONTH)
            )
            endDatePickerDialog.setMessage(getString(R.string.pick_end_date))
            endDatePickerDialog.show()
        }

    private val endDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.endDate = calendar.time

            if (!viewModel.startDate.before(viewModel.endDate)) {
                // If start date is not before end date, then we'll swap it
                val date = viewModel.startDate
                viewModel.startDate = viewModel.endDate
                viewModel.endDate = date
            }

            viewModel.updateDate(DayRange.NONE)
        }

}