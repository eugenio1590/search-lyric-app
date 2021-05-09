package com.patagonian.lyrics.domain.interactor

/**
 * Abstract interface for the presenters in the Use Cases.
 */
interface Presenter<T> {

    fun reset()

    fun success(result: T)

    fun failure(error: Throwable)
}