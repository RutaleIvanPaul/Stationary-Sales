package io.ramani.ramaniStationary.app.stock.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.home.presentation.HomeViewModel
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun initView(view: View?) {
        super.initView(view)
        available_stock_rv.layoutManager = LinearLayoutManager(requireContext())
        available_stock_rv.adapter = availableStockRVAdapter
        initSubscribers()
        initClickListeners()


    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        stock_loader.visible(visible)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_stock_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh_stock_button -> {
                viewModel.getAvailableStock()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initClickListeners() {
        search_stock.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()){
                    viewModel.availableStockProductsLiveData.postValue(viewModel.avaialableProductsListOriginal.distinct())
                }else {
                    viewModel.availableStockProductsLiveData.postValue(viewModel.avaialableProductsListOriginal.filter {
                        it.productName.contains(
                            newText,
                            ignoreCase = true
                        )
                    }.distinct())
                }

                return true
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