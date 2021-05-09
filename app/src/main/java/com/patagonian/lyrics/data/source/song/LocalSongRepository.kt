package com.patagonian.lyrics.data.source.song

import android.util.Log
import com.patagonian.lyrics.data.local.dao.SongDao
import com.patagonian.lyrics.data.local.mapper.SongMapper
import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.RecordNotFound

/**
 * Concrete implementation of the [Songs] interface for accessing to local songs data.
 *
 * @param songMapper the data object to convert [Song] models to database entities
 * @param songDao    the data access object to interact with the database
 * @constructor new instance of the local repository
 */
class LocalSongRepository(
    private val songMapper: SongMapper,
    private val songDao: SongDao
) : Songs {

    companion object {
        private val TAG = LocalSongRepository::class.java.canonicalName
    }

    override suspend fun save(song: Song) {
        try {
            val entity = songMapper.toEntity(song)
            if (!entity.isSaved) {
                songDao.insert(entity)
            } else {
                songDao.update(entity)
            }
        } catch (e: Exception) {
            Log.e(TAG, "An error has occurred saving a $song", e)
        }
    }

    override suspend fun findBy(title: String, author: String): Result<Song> {
        return try {
            val entity = songDao.findByTitleAndAuthor(title, author)
            if (entity != null) {
                Log.i(TAG, "The $entity was founded ")
                val song = songMapper.toSong(entity)
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

    override suspend fun findBySearchAtDesc(): Result<List<Song>> {
        return try {
            val songs = songDao.findBySearchAtDesc().map { songMapper.toSong(it) }
            Log.i(TAG, "${songs.size} records found getting the song history")
            Result.success(songs)
        } catch (e: Exception) {
            Log.e(TAG, "An error has occurred getting the song history")
            Result.success(emptyList())
        }
    }

}