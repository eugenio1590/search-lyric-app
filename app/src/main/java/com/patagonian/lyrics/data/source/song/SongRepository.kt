package com.patagonian.lyrics.data.source.song

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song

/**
 * Concrete implementation of the [Songs] interface
 * to access the local and remote data of the [Songs].
 *
 * @param localRepository the local data source
 * @param remoteRepository the remote data source
 * @constructor new instance of the repository [Song] with a local and remote data access
 * @see Song
 */
class SongRepository(
    private val localRepository: Songs,
    private val remoteRepository: Songs
) : Songs by localRepository {

    override suspend fun findBy(title: String, author: String): Result<Song> {
        val localResult = localRepository.findBy(title, author)
        return if (localResult.isSuccess) localResult else remoteRepository.findBy(title, author)
    }
}