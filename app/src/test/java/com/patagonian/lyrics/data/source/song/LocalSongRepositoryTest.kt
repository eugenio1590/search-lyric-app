package com.patagonian.lyrics.data.source.song

import com.patagonian.lyrics.data.local.dao.SongDao
import com.patagonian.lyrics.data.local.entity.Song as Entity
import com.patagonian.lyrics.data.local.mapper.SongMapper
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.RecordNotFound
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

class LocalSongRepositoryTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    val songMapper: SongMapper = mockk()
    val songDao: SongDao = mockk()
    val repository = LocalSongRepository(songMapper, songDao)

    given("a song title") {
        val title = "song title"

        and("a song author") {
            val author = "author name"

            and("the record exists") {
                val record = Entity(id = 4, title = title, author = author, lyric = "lyric")
                val song = Song(id = record.id, title = title, author = author, lyric = record.lyric, searchAt = record.searchAt)
                every { songDao.findByTitleAndAuthor(title, author) } returns record
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
                every { songDao.findByTitleAndAuthor(title, author) } returns null

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
                every { songDao.findByTitleAndAuthor(title, author) } throws Exception("DATABASE ERROR")

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

    given("a song information") {
        val song = Song(title = "title", author = "author", lyric = "lyric")
        val entity = Entity(title = song.title, author = song.author, lyric = song.lyric, searchAt = song.searchAt)

        and("does not exist in the database") {
            val unsavedSong = song.copy(id = 0)
            val unsavedRecord = entity.copy(id = unsavedSong.id)
            every { songMapper.toEntity(unsavedSong) } returns unsavedRecord
            justRun { songDao.insert(unsavedRecord) }

            `when`("saving the song") {
                repository.save(unsavedSong)

                then("a new record should be created") {
                    verify { songDao.insert(unsavedRecord) }
                }
            }
        }

        and("exists in the database") {
            val savedSong = song.copy(id = 1)
            val savedRecord = entity.copy(id = savedSong.id)
            every { songMapper.toEntity(any()) } returns savedRecord
            justRun { songDao.update(savedRecord) }

            `when`("saving the song") {
                repository.save(savedSong)

                then("the record should be updated") {
                    verify { songDao.update(savedRecord) }
                }
            }
        }
    }

    given("a list of saved song") {
        val record = Entity(id = 1, title = "song title", author = "author name", lyric = "lyric")
        val song = Song(id = record.id, title = record.title, author = record.author, lyric = record.lyric, searchAt = record.searchAt)
        val savedSongs = listOf(record)
        every { songDao.findBySearchAtDesc() } returns savedSongs
        every { songMapper.toSong(record) } returns song

        `when`("getting the record list sorted by descending search at") {
            val result = repository.findBySearchAtDesc()

            then("the result should be successful") {
                result.should {
                    it.isSuccess.shouldBeTrue()
                    it.getOrNull().shouldBe(listOf(song))
                }
            }
        }

        and("an error occurs") {
            every { songDao.findBySearchAtDesc() } throws Exception("DATABASE ERROR")

            `when`("getting the record list sorted by descending search at") {
                val result = repository.findBySearchAtDesc()

                then("the result should be successful") {
                    result.should {
                        it.isSuccess.shouldBeTrue()
                        it.getOrNull().shouldBe(emptyList())
                    }
                }
            }
        }
    }

    given("an empty list of saved song") {
        val savedSongs = emptyList<Entity>()
        coEvery { songDao.findBySearchAtDesc() } returns savedSongs

        `when`("getting the record list sorted by descending search at") {
            val result = repository.findBySearchAtDesc()

            then("the result should be successful") {
                result.should {
                    it.isSuccess.shouldBeTrue()
                    it.getOrNull().shouldBe(emptyList())
                }
            }
        }

        and("an error occurs") {
            every { songDao.findBySearchAtDesc() } throws Exception("DATABASE ERROR")

            `when`("getting the record list sorted by descending search at") {
                val result = repository.findBySearchAtDesc()

                then("the result should be successful") {
                    result.should {
                        it.isSuccess.shouldBeTrue()
                        it.getOrNull().shouldBe(emptyList())
                    }
                }
            }
        }
    }
})