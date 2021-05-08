package com.patagonian.lyrics.ui

sealed class UIState<out R> {
    object Initial : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Failure(val exception: Throwable) : UIState<Nothing>()
}
