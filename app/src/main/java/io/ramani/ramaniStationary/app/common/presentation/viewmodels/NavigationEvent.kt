package io.ramani.ramaniStationary.app.common.presentation.viewmodels

data class NavigationEvent(val id: Int, val tag: Any? = null)

fun buildNavigationEvent(id: Int, tag: Any? = null) =
    NavigationEvent(id, tag)