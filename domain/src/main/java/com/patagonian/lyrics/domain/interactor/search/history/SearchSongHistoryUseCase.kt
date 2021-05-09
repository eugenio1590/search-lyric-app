package com.patagonian.lyrics.domain.interactor.search.history

import com.patagonian.lyrics.domain.data.Songs
import com.patagonian.lyrics.domain.exception.DomainException.TransactionException.EmptyRecordList

internal class SearchSongHistoryUseCase(private val songs: Songs) : SearchSongHistory {

    override suspend fun execute(presenter: SearchSongHistory.Presenter) {
        songs.findBySearchAtDesc()
            .onSuccess { history ->
                presenter.takeIf { history.isNotEmpty() }?.success(history)
                presenter.takeIf { history.isEmpty() }?.failure(EmptyRecordList)
            }
    }
}