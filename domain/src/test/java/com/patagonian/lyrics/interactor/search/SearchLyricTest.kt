package com.patagonian.lyrics.interactor.search

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException
import com.patagonian.lyrics.domain.exception.DomainException.ValidationException
import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric
import com.patagonian.lyrics.util.value
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.Date

class SearchLyricTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    val presenter: SearchLyric.Presenter = mockk()
    val songs: Songs = mockk()
    val searchLyric = SearchLyric.create(songs)

    given("a song title") {
        val title = "song title"

        and("an author name") {
            val author = "author name"

            `when`("the song exists") {
                val song = Song(title = title, author = author, lyric = "lyric", searchAt = Date())
                val result: Result<Song> = Result.success(song)
                val savedSong = slot<Song>()
                val resultSong = slot<Song>()
                coEvery { songs.findBy(title, author) } returns value(result)
                coJustRun { songs.save(capture(savedSong)) }
                justRun { presenter.success(capture(resultSong)) }

                and("the use case is executed") {
                    searchLyric.execute(title, author, presenter)

                    then("the result should be successful") {
                        savedSong.should {
                            it.isCaptured.shouldBeTrue()
                            it.captured.shouldNotBe(song)
                        }
                        resultSong.should {
                            it.isCaptured.shouldBeTrue()
                            it.captured.shouldBe(savedSong.captured)
                        }
                        verify { presenter.success(resultSong.captured) }
                    }
                }
            }

            `when`("the song does not exist") {
                val error = TransactionException.RecordNotFound
                val result: Result<Song> = Result.failure(error)
                coEvery { songs.findBy(title, author) } returns value(result)
                justRun { presenter.failure(error) }

                and("the use case is executed") {
                    searchLyric.execute(title, author, presenter)

                    then("the result should be unsuccessful") {
                        verify { presenter.failure(error) }
                    }
                }
            }
        }
    }

    given("a blank song title") {
        val title = ""

        and("a blank author name") {
            val author = ""

            `when`("the use case is executed") {
                val errors = mutableListOf<Throwable>()
                justRun { presenter.failure(capture(errors)) }

                searchLyric.execute(title, author, presenter)

                then("the result should be unsuccessful") {
                    coVerify(exactly = 0) { songs.findBy(title, author) }
                    verify(exactly = 2) { presenter.failure(any()) }
                    errors.should {
                        it.shouldNotBeEmpty()
                        it.size.shouldBe(2)
                        it.first().should { titleBlankError ->
                            titleBlankError.shouldBeInstanceOf<ValidationException.BlankField>()
                            titleBlankError.field.shouldBe(Song::title.name)
                        }
                        it.last().should { authorBlankError ->
                            authorBlankError.shouldBeInstanceOf<ValidationException.BlankField>()
                            authorBlankError.field.shouldBe(Song::author.name)
                        }
                    }
                }
            }
        }
    }
})