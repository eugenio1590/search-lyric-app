package com.patagonian.lyrics.data.source.song

import android.util.Log
import com.patagonian.lyrics.data.remote.mapper.SongMapper
import com.patagonian.lyrics.data.remote.service.SongService
import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.RecordNotFound

/**
 * Concrete implementation of the [Songs] interface for accessing to remote songs data.
 *
 * @param songMapper  the data object to convert dto objects to [Song] models
 * @param songService the service object to interact with the remote server
 * @constructor new instance of the remote repository
 */
class RemoteSongRepository(
    private val songMapper: SongMapper,
    private val songService: SongService
) : Songs {

    companion object {
        private val TAG = RemoteSongRepository::class.java.canonicalName
    }

    override suspend fun save(song: Song) = throw UnsupportedOperationException()

    override suspend fun findBy(title: String, author: String): Result<Song> {
        return try {
            val result = songService.searchLyric(author, title)
            val dto = result.body()
            if (result.isSuccessful && dto != null) {
                Log.i(TAG, "The $dto was founded")
                val song = songMapper.toSong(dto).copy(title = title, author = author)
                Result.success(song)
            } else {
                Log.i(TAG, "The song was not found")
                Result.failure(RecordNotFound)
            }
        } catch (e: Exception) {
            Log.e(TAG, "An error has occurred finding a song by its title = $title and author = $author", e)
            Result.failure(RecordNotFound)
        }
    }

    override suspend fun findBySearchAtDesc(): Result<List<Song>> = throw UnsupportedOperationException()
}