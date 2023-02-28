package io.ramani.ramaniStationary.domainCore.numbers

interface NumberFormatter {
    fun format(num: Double): CharSequence?
}