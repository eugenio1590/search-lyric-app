package com.patagonian.lyrics.data.remote.service

import com.patagonian.lyrics.data.remote.dto.Song
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SongService {

    @GET("{artist}/{title}")
    suspend fun searchLyric(
        @Path("artist") author: String,
        @Path("title") title: String
    ): Response<Song>
}