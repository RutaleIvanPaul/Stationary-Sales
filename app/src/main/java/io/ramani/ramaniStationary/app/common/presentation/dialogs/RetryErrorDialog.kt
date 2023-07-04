package io.ramani.ramaniStationary.app.common.presentation.dialogs

import androidx.appcompat.app.AlertDialog
import io.ramani.ramaniStationary.R

/**
 * Created by Amr on 9/12/17.
 */
class RetryErrorDialog(errorMessage: String = "") : BaseErrorDialog(errorMessage) {
    var onRetryClick: () -> Unit = {}

    override fun setButtons(builder: AlertDialog.Builder) {
        builder.setPositiveButton(R.string.retry) { _, _ ->
            onRetryClick()
        }
    }
}