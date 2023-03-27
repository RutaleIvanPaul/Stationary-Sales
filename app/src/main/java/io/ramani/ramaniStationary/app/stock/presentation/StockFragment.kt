package io.ramani.ramaniStationary.app.stock.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.hideKeyboard
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.stock.models.response.ProductsItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_stock.*
import org.kodein.di.generic.factory

class StockFragment : BaseFragment() {

    private val viewModelProvider: (Fragment) -> StockViewModel by factory()
    private lateinit var viewModel: StockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var availableStockRVAdapter: AvailableStockRVAdapter
    private var availableStockProductsList = mutableListOf<ProductsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("Stock")
        viewModel.start()

        availableStockRVAdapter = AvailableStockRVAdapter(availableStockProductsList, onItemClick = {})

    }

    override fun onResume() {
        super.onResume()
        viewModel.getAvailableStock()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        requireActivity().findViewById<TextView>(R.id.stock_textview).visible(visible = true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun initView(view: View?) {
        super.initView(view)
        available_stock_rv.layoutManager = LinearLayoutManager(requireContext())
        available_stock_rv.adapter = availableStockRVAdapter
        initSubscribers()
        initListeners()


    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_loader.visible(visible)
    }

    private fun initListeners() {
        search_stock.addTextChangedListener(searchTextWatcher)

        search_stock.setOnEditorActionListener { v, actionId, event ->
                when(actionId){
                    EditorInfo.IME_ACTION_SEARCH -> {
                        hideKeyboard()
                        true
                    }
                    else -> false
                }
            }

        val appbar_refresh_tv = requireActivity().findViewById<Toolbar>(R.id.toolbar).findViewById<TextView>(R.id.appbar_refresh_textview)
        appbar_refresh_tv.visible(visible = true)
        appbar_refresh_tv.setOnSingleClickListener {
            viewModel.getAvailableStock()
        }
    }

    private val searchTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.toString().isNullOrEmpty()){
                viewModel.availableStockProductsLiveData.postValue(viewModel.avaialableProductsListOriginal.distinct())
            }else {
                viewModel.availableStockProductsLiveData.postValue(viewModel.avaialableProductsListOriginal.filter {
                    it.productName.contains(
                        s.toString(),
                        ignoreCase = true
                    )
                }.distinct())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        search_stock.removeTextChangedListener(searchTextWatcher)
    }

    private fun initSubscribers() {
        subscribeObservers()
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_stock

    private fun subscribeObservers(){
        viewModel.availableStockProductsLiveData.observe(this,{
            availableStockProductsList.clear()
            availableStockProductsList.addAll(it)
            availableStockRVAdapter.notifyDataSetChanged()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = StockFragment()
    }
}