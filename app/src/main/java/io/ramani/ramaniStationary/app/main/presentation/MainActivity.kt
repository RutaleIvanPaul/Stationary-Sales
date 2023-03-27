package io.ramani.ramaniStationary.app.main.presentation

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniStationary.app.common.presentation.extensions.gone
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel

import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.factory

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AuthFlow

//    private var leftMenuActionView: ActionMenuView? = null

    private val viewModelProvider: (BaseActivity) -> MainViewModel by factory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // using toolbar as ActionBar
        setSupportActionBar(toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        viewModel = viewModelProvider(this as BaseActivity)
        flow = AuthFlowController(this, R.id.main_fragment_container)
        subscribeError(viewModel)
        observerError(viewModel, this)
        viewModel.start()
        viewModel.toolbarTitleLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                appbar.gone()
            } else {
                appbar.visible()
                toolbar.title = it
            }
        }

        if (viewModel.isUserLoggedInBefore) {
            flow.openMainNav()
        } else {
            flow.openLogin()
        }
    }

}