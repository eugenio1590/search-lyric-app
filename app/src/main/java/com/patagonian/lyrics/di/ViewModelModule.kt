package com.patagonian.lyrics.di

import com.patagonian.lyrics.presentation.search.history.ViewModel as SearchSongHistoryViewModel
import com.patagonian.lyrics.presentation.search.lyric.ViewModel as SearchLyricViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchLyricViewModel() }
    viewModel { SearchSongHistoryViewModel() }
}