package io.ramani.ramaniStationary.app.history.presentation

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import io.ramani.ramaniStationary.data.history.models.response.Activity
import io.ramani.ramaniStationary.domainCore.date.getYYYYMMdd
import io.ramani.ramaniStationary.domainCore.date.getddMMMyyyy
import io.ramani.ramaniStationary.domainCore.date.today
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_history.*
import org.kodein.di.generic.factory
import java.util.*

class HistoryFragment() : BaseFragment() {

    private val viewModelProvider: (Fragment) -> HistoryViewModel by factory()
    private lateinit var viewModel: HistoryViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var historyRVAdapter: HistoryRVAdapter
    private val activityHistoryList = mutableListOf<Activity>()

    private var isAllOrdersSelected = true
    private var isStartDateSelected = true

    private var zReportPopupMenuWindow: PopupWindow? = null

    private lateinit var zReportSelectDateDialog: Dialog
    private var zReportCustomStartDate: Date? = null
    private var zReportCustomEndDate: Date? = null

    private lateinit var flow: HomeFlow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()
        setToolbarTitle("Sales History")
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        sales_history_progress_bar.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<TextView>(R.id.toolbar_title_textview)
            .visible(visible = true)
        setToolbarTitle("Sales History")
        viewModel.isThereTaxObject()
        initListeners()
        getHistory()
    }

    private fun getHistory() {
        try {
            val split = today().split("/")
            viewModel.getHistory(
                split[0].toInt(),
                viewModel.monthsArray[split[1].toInt() - 1],
                split[2].toInt()
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        requireActivity().findViewById<TextView>(R.id.toolbar_title_textview)
            .visible(visible = true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun initView(view: View?) {
        super.initView(view)
        sales_history_rv.layoutManager = LinearLayoutManager(requireContext())

        historyRVAdapter = HistoryRVAdapter(activityHistoryList, onItemClick = {
            flow.openOrderDetails(it)
        })

        sales_history_rv.adapter = historyRVAdapter

        flow = HomeFlowController(baseActivity!!, R.id.main_fragment_container)
        val customView = layoutInflater.inflate(R.layout.layout_menu_zreport, null)
        zReportPopupMenuWindow =
            PopupWindow(customView, 630, RelativeLayout.LayoutParams.WRAP_CONTENT, true)

        customView.findViewById<View>(R.id.menu_zreport_today).setOnClickListener { view: View? ->
            zReportPopupMenuWindow!!.dismiss()
            viewModel.printZreportToday()
        }

        customView.findViewById<View>(R.id.menu_zreport_yesterday)
            .setOnClickListener { view: View? ->
                zReportPopupMenuWindow!!.dismiss()
                viewModel.printZreportYesterday()
            }

        customView.findViewById<View>(R.id.menu_zreport_last_month)
            .setOnClickListener { view: View? ->
                zReportPopupMenuWindow!!.dismiss()
                viewModel.printZreportLastMonth()
            }

        customView.findViewById<View>(R.id.menu_zreport_selected_date)
            .setOnClickListener { view: View? ->
                zReportPopupMenuWindow!!.dismiss()

                showCustomZReportPopup()
            }
        initSubscribers()
        initListeners()
    }

    private fun showCustomZReportPopup() {
        zReportCustomStartDate = null
        zReportCustomEndDate = null
        (zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_start_date_label) as TextView).text =
            ""
        (zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_end_date_label) as TextView).text =
            ""
        updateZReportCustomValidate()
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_progress).visibility =
            View.GONE
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_successful_layout).visibility =
            View.GONE
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_main_layout).visibility =
            View.VISIBLE
        zReportSelectDateDialog.show()
    }

    private fun updateZReportCustomValidate() {
        val enableToConfirm = zReportCustomStartDate != null && zReportCustomEndDate != null
        val confirmButton =
            zReportSelectDateDialog.findViewById<Button>(R.id.zreport_custom_confirm_button)
        confirmButton.isEnabled = enableToConfirm
    }

    private fun initCustomZReportPopup() {
        zReportSelectDateDialog = Dialog(requireContext())
        zReportSelectDateDialog.setContentView(R.layout.dialogue_zreport_select_date)
        zReportSelectDateDialog.getWindow()!!
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        zReportSelectDateDialog.setCancelable(false)
        zReportSelectDateDialog.getWindow()!!.setGravity(Gravity.TOP);
        val wmlp = zReportSelectDateDialog.getWindow()!!.attributes
        wmlp.gravity = Gravity.CENTER
        wmlp.y = 0 //y position
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_close_button)
            .setOnClickListener { v: View? -> zReportSelectDateDialog.dismiss() }
        if (zReportCustomStartDate == null) zReportCustomStartDate = Date()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            startDateSetListener,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_start_date_pick_layout)
            .setOnClickListener { v: View? ->
                isStartDateSelected = true
                // Show date picker
                datePickerDialog.setTitle(R.string.select_start_date)
                datePickerDialog.show()

            }
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_end_date_pick_layout)
            .setOnClickListener { v: View? ->
                isStartDateSelected = false
                if (zReportCustomStartDate == null) {
                    Toast.makeText(
                        requireContext(),
                        R.string.warning_select_start_date,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    // Show date picker
                    datePickerDialog.setTitle(R.string.select_end_date)
                    datePickerDialog.show()
                }
            }
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_confirm_button)
            .setOnClickListener { v: View? ->
                if (zReportCustomStartDate == null || zReportCustomEndDate == null) {
                    Toast.makeText(
                        context,
                        getString(R.string.missing_dates),
                        Toast.LENGTH_LONG
                    ).show()
                }
                val startDate: String = getYYYYMMdd(zReportCustomStartDate!!)
                val endDate: String = getYYYYMMdd(zReportCustomEndDate!!)
                zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_progress).visibility =
                    View.VISIBLE
                zReportSelectDateDialog.dismiss()
                viewModel.printCustomZreport(startDate, endDate)
            }
        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_successful_confirm_button)
            .setOnClickListener { v: View? -> zReportSelectDateDialog.dismiss() }
    }

    private fun initListeners() {

        search_history.addTextChangedListener(searchTextWatcher)

        history_filter_all_order_button.setOnSingleClickListener {
            isAllOrdersSelected = true
            history_filter_ready_to_print_button.setBackgroundResource(R.color.transparent)
            history_filter_all_order_button.setBackgroundResource(R.drawable.filter_button_indicator)

            history_filter_all_order_button.setTextColor(resources.getColor(R.color.dark_green))
            history_filter_ready_to_print_button.setTextColor(resources.getColor(R.color.mid_dark_grey))


            viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.distinct())

        }
        history_filter_ready_to_print_button.setOnSingleClickListener {
            isAllOrdersSelected = false
            history_filter_ready_to_print_button.setBackgroundResource(R.drawable.filter_button_indicator)
            history_filter_all_order_button.setBackgroundResource(R.color.transparent)

            history_filter_all_order_button.setTextColor(resources.getColor(R.color.mid_dark_grey))
            history_filter_ready_to_print_button.setTextColor(resources.getColor(R.color.dark_green))

            viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                it.printStatus.equals("Not Printed")
            }.distinct())
        }

        val appbar_refresh_tv = requireActivity().findViewById<Toolbar>(R.id.toolbar)
            .findViewById<TextView>(R.id.appbar_refresh_textview)
        appbar_refresh_tv.visible(visible = true)
        appbar_refresh_tv.setOnSingleClickListener {
            setDatePickerTodayText()
            getHistory()
        }

        requireActivity().findViewById<TextView>(R.id.toolbar_title_textview)
            .setText("Sales History")

        setDatePickerTodayText()

        sales_history_date_picker.setOnSingleClickListener {
            DatePickerDialog(
                requireContext(),
                historyDateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        initCustomZReportPopup()
    }

    private fun setDatePickerTodayText() {
        try {
            val split = today().split("/")
            sales_history_date_picker.setText(
                String.format(
                    "%s %s %s",
                    split[0].toInt(),
                    viewModel.monthsArray[split[1].toInt() - 1],
                    split[2].toInt()
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private val historyDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            sales_history_date_picker.text =
                String.format("%s %s %s", dayOfMonth, viewModel.monthsArray.get(monthOfYear), year)
            viewModel.getHistory(dayOfMonth, viewModel.monthsArray[monthOfYear], year)
        }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            val date = GregorianCalendar(year, monthOfYear, dayOfMonth).time

            if (isStartDateSelected) {
                // If date is not past day including today, then it'll be ignored
                val differenceInMiliseconds: Long =
                    Date().time - date.time
                if (differenceInMiliseconds < 0 || getYYYYMMdd(date)
                        .equals(getYYYYMMdd(Date()))
                ) {
                    Toast.makeText(
                        context,
                        R.string.warning_future_date,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (zReportCustomEndDate != null && !date.before(zReportCustomEndDate)) {
                    Toast.makeText(
                        context,
                        R.string.warning_date_validation,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    zReportCustomStartDate = date
                    val textView =
                        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_start_date_label) as TextView
                    textView.setText(getddMMMyyyy(zReportCustomStartDate))
                    textView.setTextColor(Color.BLACK)

                    // Check validation
                    updateZReportCustomValidate()
                }
            } else {
                if (zReportCustomStartDate != null && zReportCustomStartDate?.before(date) == false) {
                    Toast.makeText(
                        context,
                        R.string.warning_date_validation,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    zReportCustomEndDate = date
                    val textView =
                        zReportSelectDateDialog.findViewById<View>(R.id.zreport_custom_end_date_label) as TextView
                    textView.setText(getddMMMyyyy(zReportCustomEndDate))
                    textView.setTextColor(Color.BLACK)

                    // Check validation
                    updateZReportCustomValidate()
                }

            }
        }

    private val searchTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().isEmpty()) {
                if (isAllOrdersSelected) {
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.distinct())
                } else {
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                        it.printStatus.equals("Not Printed")
                    }.distinct())
                }
            } else {
                if (isAllOrdersSelected) {
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                        it.locationName!!.contains(
                            s.toString(),
                            ignoreCase = true
                        )
                    }.distinct())
                } else {
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                        it.printStatus.equals("Not Printed")
                    }.filter {
                        it.locationName!!.contains(
                            s.toString(),
                            ignoreCase = true
                        )
                    }.distinct())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (search_history.isNotNull()) {
            search_history.removeTextChangedListener(searchTextWatcher)
        }
    }

    private fun subscribeObservers() {
        viewModel.historyActivityLiveData.observe(this) {
            activityHistoryList.clear()
            if (isAllOrdersSelected) {
                activityHistoryList.addAll(it)
            } else {
                activityHistoryList.addAll(it.filter {
                    it.printStatus.equals("Not Printed")
                })
            }
            historyRVAdapter.notifyDataSetChanged()
        }

        viewModel.historySummaryLiveData.observe(this) {
            total_sales_history_value.setText(
                String.format(
                    "%s %s",
                    it.totalSpend.toString(),
                    viewModel.currency
                )
            )
            total_discount_value.setText(
                String.format(
                    "%s %s",
                    it.totalDiscountValue,
                    viewModel.currency
                )
            )
            total_order_value.setText(it.numOrders.toString())
        }

        viewModel.isThereTaxObject.observe(this) {
            if (it) {
                sales_history_print_zreport.setOnSingleClickListener {
                    //print Z report
                    zReportPopupMenuWindow?.showAsDropDown(it, 0, 5)

                }
                sales_history_date_picker_xreport.setOnSingleClickListener {
                    //print X report
                    viewModel.printXreport()
                }
            } else {
                sales_history_print_zreport.setOnSingleClickListener {
                    //cannot print Z report
                    showError("Cannot Print Z Report. No Tax Information")
                }
                sales_history_date_picker_xreport.setOnSingleClickListener {
                    //cannot print X report
                    showError("Cannot Print X Report. No Tax Information")
                }
            }
        }
    }

    private fun initSubscribers() {
        subscribeObservers()
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_history

    companion object {
        fun newInstance() =
            HistoryFragment()
    }
}