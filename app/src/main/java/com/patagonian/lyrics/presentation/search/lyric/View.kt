package com.patagonian.lyrics.presentation.search.lyric

/**
 * Basic interface that should be implemented by the UI to search a lyric of a song.
 */
interface View {

    val mandatoryFieldError: String

    fun isNetworkConnectivityAvailable(): Boolean
}