package com.patagonian.lyrics.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("lyrics")
    val lyric: String
)