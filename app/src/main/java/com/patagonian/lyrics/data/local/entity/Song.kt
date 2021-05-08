package com.patagonian.lyrics.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = DEFAULT_ID,
    @ColumnInfo val title: String,
    @ColumnInfo val author: String,
    @ColumnInfo val lyric: String,
    @ColumnInfo(name = "search_at") val searchAt: Date = Date()
) {
    companion object {
        private const val DEFAULT_ID = 0
    }

    val isSaved: Boolean
        get() = id != DEFAULT_ID
}