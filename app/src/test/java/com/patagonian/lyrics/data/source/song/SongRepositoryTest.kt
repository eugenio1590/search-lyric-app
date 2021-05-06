package com.patagonian.lyrics.data.source.song

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException
import com.patagonian.lyrics.util.value
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class SongRepositoryTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    val localRepository = mockk<Songs>()
    val remoteRepository = mockk<Songs>()
    val songsRepository = SongRepository(localRepository, remoteRepository)

    given("a song title") {
        val title = "song title"

        and("an author name") {
            val author = "author name"

            and("the local record exists") {
                val song = Song(title = title, author = author, lyric = "lyric")
                val expectedResult = Result.success(song)
                coEvery { localRepository.findBy(title, author) } returns value(expectedResult)

                `when`("searching is performed") {
                    val result = songsRepository.findBy(title, author)

                    then("the local record should be returned") {
                        coVerify(exactly = 1) { localRepository.findBy(title, author) }
                        coVerify(exactly = 0) { remoteRepository.findBy(title, author) }
                        result.shouldBe(expectedResult)
                    }
                }
            }

            and("the local record does not exist") {
                val recordNotFound = TransactionException.RecordNotFound
                val expectedError: Result<Song> = Result.failure(recordNotFound)
                println("Init local repository")
                coEvery { localRepository.findBy(title, author) } returns value(expectedError)

                and("the remote record exists") {
                    val song = Song(title = title, author = author, lyric = "lyric")
                    val expectedResult = Result.success(song)
                    coEvery { remoteRepository.findBy(title, author) } returns value(expectedResult)

                    `when`("searching is performed") {
                        val result = songsRepository.findBy(title, author)

                        then("the remote record should be returned") {
                            coVerify(exactly = 1) { localRepository.findBy(title, author) }
                            coVerify(exactly = 1) { remoteRepository.findBy(title, author) }
                            result.shouldBe(expectedResult)
                        }
                    }
                }

                and("the remote record does not exist") {
                    coEvery { remoteRepository.findBy(title, author) } returns value(expectedError)

                    `when`("searching is performed") {
                        val result = songsRepository.findBy(title, author)

                        then("an error should be returned") {
                            coVerify(exactly = 1) { localRepository.findBy(title, author) }
                            coVerify(exactly = 1) { remoteRepository.findBy(title, author) }
                            result.shouldBe(expectedError)
                        }
                    }
                }
            }
        }
    }

    given("a song data") {
        val song = Song(title = "title", author = "author", lyric = "lyric")

        `when`("saving is performed") {
            coJustRun { localRepository.save(song) }
            songsRepository.save(song)

            then("the record should be saved in the local repository") {
                coVerify(exactly = 1) { localRepository.save(song) }
                coVerify(exactly = 0) { remoteRepository.save(song) }
            }
        }
    }
})