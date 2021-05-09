package com.patagonian.lyrics.di

import com.patagonian.lyrics.domain.interactor.search.lyric.SearchLyric
import org.koin.dsl.module

val useCaseModule = module {
    factory { SearchLyric.create(get()) }
}