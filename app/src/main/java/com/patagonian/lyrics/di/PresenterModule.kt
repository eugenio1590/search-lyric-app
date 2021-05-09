package com.patagonian.lyrics.di

import com.patagonian.lyrics.domain.interactor.search.history.SearchSongHistory
import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric
import com.patagonian.lyrics.presentation.search.history.ViewModel as SearchSongHistoryViewModel
import com.patagonian.lyrics.presentation.search.history.Presenter as SearchSongHistoryPresenter
import com.patagonian.lyrics.presentation.search.lyric.ViewModel as SearchLyricViewModel
import com.patagonian.lyrics.presentation.search.lyric.View as SearchLyricView
import com.patagonian.lyrics.presentation.search.lyric.Presenter as SearchLyricPresenter
import org.koin.dsl.module

val presenterModule = module {
    factory<SearchLyric.Presenter> { (view: SearchLyricView, viewModel: SearchLyricViewModel) ->
        SearchLyricPresenter(view, viewModel, get())
    }
    factory<SearchSongHistory.Presenter> { (viewModel: SearchSongHistoryViewModel) ->
        SearchSongHistoryPresenter(viewModel, get())
    }
}