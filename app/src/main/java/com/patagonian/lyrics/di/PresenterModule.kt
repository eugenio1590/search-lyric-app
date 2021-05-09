package com.patagonian.lyrics.di

import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric
import com.patagonian.lyrics.presentation.search.lyric.ViewModel as SearchLyricViewModel
import com.patagonian.lyrics.presentation.search.lyric.View as SearchLyricView
import com.patagonian.lyrics.presentation.search.lyric.Presenter as SearchLyricPresenter
import org.koin.dsl.module

val presenterModule = module {
    factory<SearchLyric.Presenter> { (view: SearchLyricView, viewModel: SearchLyricViewModel) ->
        SearchLyricPresenter(view, viewModel, get())
    }
}