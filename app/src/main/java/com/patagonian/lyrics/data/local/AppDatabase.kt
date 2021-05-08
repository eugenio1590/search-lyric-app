package com.patagonian.lyrics.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.patagonian.lyrics.data.local.converter.DateConverter
import com.patagonian.lyrics.data.local.dao.SongDao
import com.patagonian.lyrics.data.local.entity.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "songs-database"

        fun create(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }

    abstract fun songDao(): SongDao
}