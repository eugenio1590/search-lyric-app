package com.patagonian.lyrics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.patagonian.lyrics.data.local.entity.Song

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: Song)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(song: Song)

    @Query("SELECT * FROM song WHERE title LIKE '%' || :title || '%' AND author LIKE '%' || :author || '%' LIMIT 1")
    fun findByTitleAndAuthor(title: String, author: String): Song?
}