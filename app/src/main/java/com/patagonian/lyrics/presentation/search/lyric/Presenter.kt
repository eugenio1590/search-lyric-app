package com.patagonian.lyrics.presentation.search.lyric

import com.patagonian.lyrics.domain.entity.Song
import com.patagonian.lyrics.domain.exception.DomainException.NetworkException.NetworkNotAvailable
import com.patagonian.lyrics.domain.exception.DomainException.ValidationException.BlankField
import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric

/**
 * Concrete class to present the response of the song lyric searching and the happened errors.
 *
 * @param view      the user interface
 * @param viewModel the user data container
 * @param useCase   the class for searching a lyric of a song
 * @see SearchLyric
 */
class Presenter(
    private val view: View,
    private val viewModel: ViewModel,
    override val useCase: SearchLyric,
) : SearchLyric.Presenter {

    override fun reset() = viewModel.reset()

    override suspend fun search() {
        val isNetworkConnectivityAvailable = view.isNetworkConnectivityAvailable()
        if (isNetworkConnectivityAvailable) {
            val title = viewModel.songTitle.value ?: return
            val author = viewModel.artistName.value ?: return
            viewModel.setAction(ViewModel.Action.SEARCH_SONG)
            useCase.execute(title, author, this)
        } else {
            viewModel.failure(NetworkNotAvailable)
        }
    }

    override fun success(result: Song) {
        viewModel.success(result)
    }

    override fun failure(error: Throwable) {
        when (error) {
            is BlankField -> viewModel.setFieldError(error.field, view.mandatoryFieldError)
            else -> viewModel.failure(error)
        }
    }
}