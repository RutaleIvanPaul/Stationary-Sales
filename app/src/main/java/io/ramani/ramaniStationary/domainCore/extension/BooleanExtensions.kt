package io.ramani.ramaniStationary.domainCore.extension

fun Boolean.toReplyText() =
    when (this) {
        true -> {
            "Yes"
        }
        false -> {
            "No"
        }
    }