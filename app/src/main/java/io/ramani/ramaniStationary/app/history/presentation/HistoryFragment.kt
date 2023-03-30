package io.ramani.ramaniStationary.app.history.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.history.models.response.Activity
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_stock.*
import org.kodein.di.generic.factory

class HistoryFragment() : BaseFragment() {

    private val viewModelProvider: (Fragment) -> HistoryViewModel by factory()
    private lateinit var viewModel: HistoryViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var historyRVAdapter: HistoryRVAdapter
    private val activityHistoryList = mutableListOf<Activity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("Sales History")
        viewModel.start()

        historyRVAdapter = HistoryRVAdapter(activityHistoryList, onItemClick = {})
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        sales_history_progress_bar.visible(visible)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        requireActivity().findViewById<TextView>(R.id.stock_textview).visible(visible = true)
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
            history_filter_ready_to_print_button.setBackgroundResource(R.color.transparent)
            history_filter_all_order_button.setBackgroundResource(R.drawable.filter_button_indicator)

            history_filter_all_order_button.setTextColor(resources.getColor(R.color.dark_green))
            history_filter_ready_to_print_button.setTextColor(resources.getColor(R.color.mid_dark_grey))


            viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.distinct())

        }
        history_filter_ready_to_print_button.setOnSingleClickListener {
            history_filter_ready_to_print_button.setBackgroundResource(R.drawable.filter_button_indicator)
            history_filter_all_order_button.setBackgroundResource(R.color.transparent)

            history_filter_all_order_button.setTextColor(resources.getColor(R.color.mid_dark_grey))
            history_filter_ready_to_print_button.setTextColor(resources.getColor(R.color.dark_green))

            viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                it.printStatus.equals("Not Printed")
            }.distinct())
        }
    }

    private val searchTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().isNullOrEmpty()){
                viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.distinct())
            }else {
                viewModel.historyActivityLiveData.postValue(viewModel.historyActivityListOriginal.filter {
                    it?.locationName!!.contains(
                        s.toString(),
                        ignoreCase = true
                    )
                }.distinct())
            }
        }
    }

    private fun subscribeObservers(){
        viewModel.historyActivityLiveData.observe(this,{
            activityHistoryList.clear()
            activityHistoryList.addAll(it)
            historyRVAdapter.notifyDataSetChanged()
        })

        viewModel.historySummaryLiveData.observe(this,{
            total_sales_history_value.setText(it.totalSpend.toString())
            total_order_value.setText(it.numOrders.toString())
        })

        viewModel.isThereTaxObject.observe(this,{
            if (it){
                sales_history_date_picker.setOnSingleClickListener {
                    //print Z report
                    Toast.makeText(requireContext(),"Printing ...",Toast.LENGTH_LONG)
                }
                sales_history_date_picker_xreport.setOnSingleClickListener {
                    //print X report
                    Toast.makeText(requireContext(),"Printing ...",Toast.LENGTH_LONG)
                }
            }
            else{
                sales_history_date_picker.setOnSingleClickListener {
                    //cannot print Z report
                    Toast.makeText(requireContext(),"Cannot Print. No Tax Information",Toast.LENGTH_LONG)
                }
                sales_history_date_picker_xreport.setOnSingleClickListener {
                    //cannot print X report
                    Toast.makeText(requireContext(),"Cannot Print. No Tax Information",Toast.LENGTH_LONG)
                }
            }
        })
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