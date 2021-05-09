package com.patagonian.lyrics.domain.interactor.search.lyric

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.ValidationException
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException

/**
 * Contract of the use case for searching a lyric of a song.
 * Use the [execute] function to perform this use case.
 *
 * The end-point is determined by the class [Presenter].
 */
interface SearchLyric {

    companion object {
        /**
         * Method to get an instance of this use case.
         */
        fun create(songs: Songs): SearchLyric = SearchLyricUseCase(songs)
    }

    /**
     * Method to perform this use case.
     *
     * @param title the song title
     * @param author the author name
     * @throws ValidationException.BlankField - if any parameter is blank
     * @throws TransactionException.RecordNotFound - if the song does not exist
     */
    suspend fun execute(title: String, author: String, presenter: Presenter)

    interface Presenter : com.patagonian.lyrics.domain.interactor.Presenter<Song> {

        val useCase: SearchLyric

        /**
         * Method to reset the search.
         */
        fun reset()

        /**
         * Method to execute the [useCase].
         *
         * @see [execute]
         */
        suspend fun search()
    }
}