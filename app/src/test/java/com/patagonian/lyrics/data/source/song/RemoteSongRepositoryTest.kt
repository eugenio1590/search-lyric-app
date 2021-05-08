package com.patagonian.lyrics.data.source.song

import com.patagonian.lyrics.data.remote.dto.Song as Dto
import com.patagonian.lyrics.data.remote.mapper.SongMapper
import com.patagonian.lyrics.data.remote.service.SongService
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.RecordNotFound
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteSongRepositoryTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    val songMapper: SongMapper = mockk()
    val songService: SongService = mockk()
    val repository = RemoteSongRepository(songMapper, songService)

    given("a song title") {
        val title = "song title"

        and("a song author") {
            val author = "author name"

            and("the record exists") {
                val record = Dto(lyric = "lyric")
                val song = Song(title = title, author = author, lyric = record.lyric)
                coEvery { songService.searchLyric(author, title) } returns Response.success(record)
                every { songMapper.toSong(record) } returns song

                `when`("searching the record by its title and author") {
                    val result = repository.findBy(title, author)

                    then("the result should be successful") {
                        result.should {
                            it.isSuccess.shouldBeTrue()
                            it.getOrNull().shouldBe(song)
                        }
                    }
                }
            }

            and("the record does not exist") {
                val body: ResponseBody = mockk(relaxed = true)
                coEvery { songService.searchLyric(author, title) } returns Response.error(404, body)

                `when`("searching the record by its title and author") {
                    val result = repository.findBy(title, author)

                    then("the result should be unsuccessful") {
                        result.should {
                            it.isFailure.shouldBeTrue()
                            it.exceptionOrNull().shouldBe(RecordNotFound)
                        }
                    }
                }
            }

            and("an error occurs") {
                coEvery { songService.searchLyric(author, title) } throws Exception("NETWORK ERROR")

                `when`("searching the record by its title and author") {
                    val result = repository.findBy(title, author)

                    then("the result should be unsuccessful") {
                        result.should {
                            it.isFailure.shouldBeTrue()
                            it.exceptionOrNull().shouldBe(RecordNotFound)
                        }
                    }
                }
            }
        }
    }
})