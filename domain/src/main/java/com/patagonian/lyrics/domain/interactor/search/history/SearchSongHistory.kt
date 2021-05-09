package com.patagonian.lyrics.domain.interactor.search.history

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException

/**
 * Contract of the use case for searching the song history.
 * Use the [execute] function to perform this use case.
 *
 * The end-point is determined by the class [Presenter].
 */
interface SearchSongHistory {

    companion object {
        /**
         * Method to get an instance of this use case.
         *
         * @throws TransactionException.EmptyRecordList - if the history is empty
         */
        fun create(songs: Songs): SearchSongHistory = SearchSongHistoryUseCase(songs)
    }

    /**
     * Method to perform this use case.
     */
    suspend fun execute(presenter: Presenter)

    interface Presenter : com.patagonian.lyrics.domain.interactor.Presenter<List<Song>>
}