package io.ramani.ramaniStationary.domainCore.entities

/**
 * Created by Amr on 10/16/17.
 */
interface IBuilder<out T> {
    fun build(): T
}