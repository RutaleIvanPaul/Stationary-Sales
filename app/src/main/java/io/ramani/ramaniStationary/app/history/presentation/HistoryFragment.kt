package io.ramani.ramaniStationary.app.history.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.history.models.response.Activity
import io.ramani.ramaniStationary.domainCore.date.today
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import kotlinx.android.synthetic.main.fragment_history.*
import org.kodein.di.generic.factory
import java.util.Calendar

class HistoryFragment() : BaseFragment() {

    private val viewModelProvider: (Fragment) -> HistoryViewModel by factory()
    private lateinit var viewModel: HistoryViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var historyRVAdapter: HistoryRVAdapter
    private val activityHistoryList = mutableListOf<Activity>()

    private var isAllOrdersSelected = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        viewModel.start()
        setToolbarTitle("Sales History")
        historyRVAdapter = HistoryRVAdapter(activityHistoryList, onItemClick = {})
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
        viewModel.isThereTaxObject()
        initListeners()
        getHistory()
    }

    private fun getHistory() {
        val split = today().split("/")
        viewModel.getHistory(
            split.get(0).toInt(),
            viewModel.monthsArray.get(split.get(1).toInt() - 1),
            split.get(2).toInt()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        requireActivity().findViewById<TextView>(R.id.toolbar_title_textview).visible(visible = true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun initView(view: View?) {
        super.initView(view)
        sales_history_rv.layoutManager = LinearLayoutManager(requireContext())
        sales_history_rv.adapter = historyRVAdapter
        initSubscribers()
        initListeners()
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

        val appbar_refresh_tv = requireActivity().findViewById<Toolbar>(R.id.toolbar).findViewById<TextView>(R.id.appbar_refresh_textview)
        appbar_refresh_tv.visible(visible = true)
        appbar_refresh_tv.setOnSingleClickListener {
            setDatePickerTodayText()
            getHistory()
        }

        requireActivity().findViewById<TextView>(R.id.toolbar_title_textview).setText("Sales History")

        setDatePickerTodayText()

        sales_history_date_picker.setOnSingleClickListener {
            DatePickerDialog(
                requireContext(),
                startDateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setDatePickerTodayText() {
        val split = today().split("/")
        sales_history_date_picker.setText(
            String.format(
                "%s %s %s",
                split.get(0).toInt(),
                viewModel.monthsArray.get(split.get(1).toInt() - 1),
                split.get(2).toInt()
            )
        )
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            sales_history_date_picker.setText(String.format("%s %s %s",dayOfMonth,viewModel.monthsArray.get(monthOfYear),year))
            viewModel.getHistory(dayOfMonth,viewModel.monthsArray.get(monthOfYear),year)
        }

    private val searchTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().isNullOrEmpty()){
                if(isAllOrdersSelected) {
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.distinct())
                }
                else{
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                        it.printStatus.equals("Not Printed")
                    }.distinct())
                }
            }else {
                if (isAllOrdersSelected) {
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                        it?.locationName!!.contains(
                            s.toString(),
                            ignoreCase = true
                        )
                    }.distinct())
                }
                else{
                    viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                        it.printStatus.equals("Not Printed")
                    }.filter {
                        it?.locationName!!.contains(
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
        if(search_history.isNotNull()) {
            search_history.removeTextChangedListener(searchTextWatcher)
        }
    }

    private fun subscribeObservers(){
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
            total_sales_history_value.setText(it.totalSpend.toString())
            total_order_value.setText(it.numOrders.toString())
        }

        viewModel.isThereTaxObject.observe(this) {
            if (it) {
                sales_history_date_picker_zreport.setOnSingleClickListener {
                    //print Z report
                    Toast.makeText(requireContext(), "Printing ...", Toast.LENGTH_LONG)
                }
                sales_history_date_picker_xreport.setOnSingleClickListener {
                    //print X report
                    Toast.makeText(requireContext(), "Printing ...", Toast.LENGTH_LONG)
                }
            } else {
                sales_history_date_picker_zreport.setOnSingleClickListener {
                    //cannot print Z report
                    Toast.makeText(
                        requireContext(),
                        "Cannot Print. No Tax Information",
                        Toast.LENGTH_LONG
                    )
                }
                sales_history_date_picker_xreport.setOnSingleClickListener {
                    //cannot print X report
                    Toast.makeText(
                        requireContext(),
                        "Cannot Print. No Tax Information",
                        Toast.LENGTH_LONG
                    )
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