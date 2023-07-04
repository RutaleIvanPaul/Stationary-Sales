package io.ramani.ramaniStationary.app.common.presentation.adapters

import android.content.Context
import android.content.res.Configuration
import io.ramani.ramaniStationary.app.common.presentation.extensions.isTablet

object SpanHelper {
    private const val PHONE_PORTRAIT_SPAN_COUNT = 2
    private const val TABLET_PORTRAIT_SPAN_COUNT = 3
    private const val TABLET_LANDSCAPE_SPAN_COUNT = 4

    fun getGridSpanCount(context: Context?): Int {
        val config = context?.resources?.configuration
        var spanCount = PHONE_PORTRAIT_SPAN_COUNT
        val isTablet = context?.isTablet() ?: false
        if (isTablet) {
            when (config?.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> spanCount = TABLET_PORTRAIT_SPAN_COUNT
                Configuration.ORIENTATION_LANDSCAPE -> spanCount = TABLET_LANDSCAPE_SPAN_COUNT
            }
        }
        return spanCount
    }
}