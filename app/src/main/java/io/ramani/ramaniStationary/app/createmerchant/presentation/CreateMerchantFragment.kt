package io.ramani.ramaniStationary.app.createmerchant.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.hideKeyboard
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createmerchant.presentation.adapter.CreateMerchantRVAdapter
import io.ramani.ramaniStationary.app.createmerchant.presentation.dialog.CreateNewMerchantDialog
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import kotlinx.android.synthetic.main.fragment_create_merchant.*
import org.kodein.di.generic.factory

class CreateMerchantFragment : BaseFragment() {
    companion object {
        fun newInstance() = CreateMerchantFragment()
    }

    private val viewModelProvider: (Fragment) -> CreateMerchantViewModel by factory()
    private lateinit var viewModel: CreateMerchantViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    override fun getLayoutResId(): Int = R.layout.fragment_create_merchant
    private lateinit var merchantAdapter: CreateMerchantRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        create_merchant_search_textfield.addTextChangedListener(searchTextWatcher)
        create_merchant_search_textfield.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }

        create_merchant_add_new.setOnSingleClickListener {
            createNewMerchant()
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
            updateRV()
        }
    }

    override fun onDestroy() {
        if (isAdded)
            create_merchant_search_textfield.removeTextChangedListener(searchTextWatcher)

        super.onDestroy()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        create_merchant_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun createNewMerchant() {
        val dialog = CreateNewMerchantDialog(requireActivity(), viewModel, this) { merchant ->
            updateRV()
        }

        dialog.show()
    }

    private fun updateRV() {
        val keyword = create_merchant_search_textfield.text.trim().toString()
        val merchants =
            if (keyword.isNotEmpty())
                viewModel.merchantList.filter { merchant -> merchant.name.contains(keyword, true) }
            else
                viewModel.merchantList

        merchantAdapter = CreateMerchantRVAdapter(merchants.sortedByDescending { merchant -> merchant.updatedAt } as MutableList<MerchantModel>, viewModel.topMerchants) { position, item ->

        }

        create_merchant_merchant_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = merchantAdapter
        }
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            updateRV()
        }
    }
}