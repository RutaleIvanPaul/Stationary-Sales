package io.ramani.ramaniStationary.app.stock.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.home.presentation.HomeViewModel
import org.kodein.di.generic.factory

class StockFragment : BaseFragment() {

    private val viewModelProvider: (Fragment) -> StockViewModel by factory()
    private lateinit var viewModel: StockViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("Stock")
        viewModel.start()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_stock

    companion object {
        @JvmStatic
        fun newInstance() = StockFragment()
    }
}