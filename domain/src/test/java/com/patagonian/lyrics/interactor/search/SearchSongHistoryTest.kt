package com.patagonian.lyrics.interactor.search

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException
import com.patagonian.lyrics.domain.interactor.search.history.SearchSongHistory
import com.patagonian.lyrics.util.value
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class SearchSongHistoryTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    val presenter: SearchSongHistory.Presenter = mockk()
    val songs: Songs = mockk()
    val searchSongHistory = SearchSongHistory.create(songs)

    given("a list of saved song") {
        val song = Song(title = "song title", author = "author name", lyric = "lyric")
        val savedSongs = listOf(song)
        val result: Result<List<Song>> = Result.success(savedSongs)
        coEvery { songs.findBySearchAtDesc() } returns value(result)
        justRun { presenter.success(savedSongs) }

        `when`("the use case is executed") {
            searchSongHistory.execute(presenter)

            then("the result should be successful") {
                verify { presenter.success(savedSongs) }
            }
        }
    }

    given("an empty list of saved song") {
        val savedSongs = emptyList<Song>()
        val result: Result<List<Song>> = Result.success(savedSongs)
        val error = TransactionException.EmptyRecordList
        coEvery { songs.findBySearchAtDesc() } returns value(result)
        justRun { presenter.failure(error) }

        `when`("the use case is executed") {
            searchSongHistory.execute(presenter)

            then("the result should be unsuccessful") {
                verify { presenter.failure(error) }
            }
        }
    }
})