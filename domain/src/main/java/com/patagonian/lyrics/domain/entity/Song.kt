package com.patagonian.lyrics.domain.entity

import java.util.Date

/**
 * Business entity that contains the information of an artist's song.
 */
data class Song(
    val title: String,
    val author: String,
    val lyric: String,
    val searchAt: Date = Date()
)