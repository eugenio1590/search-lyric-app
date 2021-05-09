package com.patagonian.lyrics.domain.data

import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException

/**
 * Main entry point for accessing songs data.
 */
interface Songs {

    /**
     * Method to save the song data in the data source.
     *
     * @param song the song data
     */
    suspend fun save(song: Song)

    /**
     * Method to get the song record based on its [title] and its [author].
     *
     * @param title the song title
     * @param author the song author
     * @return the [Song] record - if it exists
     * @throws TransactionException.RecordNotFound - if the record is not found
     * @see Song
     */
    suspend fun findBy(title: String, author: String): Result<Song>

    /**
     * Method to get a list of a [Song] sorted by search date descending.
     *
     * @return the [Song] list
     * @see Song
     */
    suspend fun findBySearchAtDesc(): Result<List<Song>>
}