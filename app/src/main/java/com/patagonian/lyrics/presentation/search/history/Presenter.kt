package com.patagonian.lyrics.presentation.search.history

import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.interactor.search.history.SearchSongHistory

/**
 * Concrete class to present the song history and the happened errors.
 *
 * @param viewModel the user data container
 * @param useCase   the class for searching the song history
 */
class Presenter(
    private val viewModel: ViewModel,
    override val useCase: SearchSongHistory
) : SearchSongHistory.Presenter {

    override fun reset() {
        viewModel.reset()
    }

    override suspend fun search() {
        viewModel.setAction(ViewModel.Action.SEARCH_HISTORY)
        super.search()
    }

    override fun success(result: List<Song>) {
        viewModel.success(result)
    }

    override fun failure(error: Throwable) {
        viewModel.failure(error)
    }
}