package com.patagonian.lyrics.domain.interactor.search.lyric

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.ValidationException.BlankField
import java.util.Date

/**
 * Concrete implementation of the use case [SearchLyric].
 *
 * @param songs the main entry point to access to the songs data.
 * @constructor new instance of the use case [SearchLyric]
 * @see SearchLyric
 */
internal class SearchLyricUseCase(private val songs: Songs) : SearchLyric {

    override suspend fun execute(title: String, author: String, presenter: SearchLyric.Presenter) {
        presenter.takeIf { title.isBlank() }?.failure(BlankField(Song::title.name))
        presenter.takeIf { author.isBlank() }?.failure(BlankField(Song::author.name))

        songs.takeIf { title.isNotBlank() and author.isNotBlank() }?.findBy(title, author)
            ?.onSuccess {
                val song = it.copy(searchAt = Date())
                songs.save(song)
                presenter.success(song)
            }
            ?.onFailure { error -> presenter.failure(error) }
    }
}