package io.ramani.ramaniStationary.app.home.presentation

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createorder.presentation.CREATE_ORDER_MODEL
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import io.ramani.ramaniStationary.app.main.presentation.MAIN_SHARED_MODEL
import kotlinx.android.synthetic.main.fragment_home_host.*
import org.kodein.di.generic.factory

/**
 * Home host fragment
 *
 * This fragment will contains several sub pages inside its fragment container
 *
 */
class HomeHostFragment : BaseFragment() {
    companion object {
        fun newInstance() = HomeHostFragment()
    }

    enum class Page {
        NONE, HOME, STOCK, HISTORY, CREDIT
    }

    private val viewModelProvider: (Fragment) -> HomeViewModel by factory()
    private lateinit var viewModel: HomeViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: HomeFlow
    private lateinit var authFlow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_home_host

    private var rootPage = Page.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = HomeFlowController(baseActivity!!, R.id.home_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        setupBottomTab()

        initSubscribers()

        gotoPage(Page.HOME)
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.start()
        viewModel.syncData()
    }

    private fun subscribeResponse() {
        viewModel.onDataSyncStartedLiveData.observe(this) {
            home_host_data_sync_layout.visible(true)
        }

        viewModel.onDataSyncUpdateLiveData.observe(this) {
            home_host_progress.apply {
                max = it.first
                progress = it.second
            }
        }

        viewModel.onDataSyncCompletedLiveData.observe(this) {
            home_host_data_sync_layout.visible(false)
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        home_host_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun setupBottomTab() {
        home_tab_refresh.setOnSingleClickListener {
            viewModel.syncData()
        }

        home_nav_home.setOnSingleClickListener {
            if (canNavigate())
                gotoPage(Page.HOME)
        }

        home_nav_stock.setOnSingleClickListener {
            if (canNavigate())
                gotoPage(Page.STOCK)
        }

        home_nav_history.setOnSingleClickListener {
            if (canNavigate())
                gotoPage(Page.HISTORY)
        }

        home_nav_credit.setOnSingleClickListener {
            if (canNavigate())
                gotoPage(Page.CREDIT)
        }
    }

    private fun gotoPage(page: Page) {
        if (rootPage == page)
            return

        bottom_nav_home_icon.setImageResource(R.mipmap.ic_home)
        bottom_nav_stock_icon.setImageResource(R.mipmap.ic_home_stock)
        bottom_nav_history_icon.setImageResource(R.mipmap.ic_home_history)
        bottom_nav_credit_icon.setImageResource(R.mipmap.ic_home_credit)

        val colorRamaniGreen = ContextCompat.getColor(requireContext(), R.color.ramani_green)
        val colorNavTextGrey = ContextCompat.getColor(requireContext(), R.color.colorNavTextGrey)

        bottom_nav_home_text.setTextColor(colorNavTextGrey)
        bottom_nav_stock_text.setTextColor(colorNavTextGrey)
        bottom_nav_history_text.setTextColor(colorNavTextGrey)
        bottom_nav_credit_text.setTextColor(colorNavTextGrey)

        when(page) {
            Page.STOCK -> {
                bottom_nav_stock_icon.setImageResource(R.mipmap.ic_home_stock_green)
                bottom_nav_stock_text.setTextColor(colorRamaniGreen)

                flow.openStock()
            }

            Page.HISTORY -> {
                bottom_nav_history_icon.setImageResource(R.mipmap.ic_home_history_green)
                bottom_nav_history_text.setTextColor(colorRamaniGreen)

                flow.openHistory()
            }

            Page.CREDIT -> {
                bottom_nav_credit_icon.setImageResource(R.mipmap.ic_home_credit_green)
                bottom_nav_credit_text.setTextColor(colorRamaniGreen)

                flow.openCredit()
            }

            else -> {
                // Default is home
                bottom_nav_home_icon.setImageResource(R.mipmap.ic_home_green)
                bottom_nav_home_text.setTextColor(colorRamaniGreen)

                flow.openHome()
            }
        }

        rootPage = page
    }

    override fun onBackButtonPressed(): Boolean {
        // When press back button on home page, the logout alert will be shown and no need to back from stack
        //flow.onBackPressed()

        if (rootPage != Page.HOME) {
            gotoPage(Page.HOME)
        } else {
            requireActivity().finish()
        }

        return true
    }

    private fun canNavigate() =
        if (MAIN_SHARED_MODEL.isSynching) {
            errorDialog("Data sync is being done. Please wait until it's finished.")
            false
        } else {
            true
        }
}