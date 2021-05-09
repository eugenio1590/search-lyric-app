package com.patagonian.lyrics.di

import com.patagonian.lyrics.data.local.AppDatabase
import com.patagonian.lyrics.data.remote.Api
import com.patagonian.lyrics.data.remote.mapper.SongMapper as RemoteSongMapper
import com.patagonian.lyrics.data.remote.service.SongService
import com.patagonian.lyrics.data.source.song.LocalSongRepository
import com.patagonian.lyrics.data.source.song.RemoteSongRepository
import com.patagonian.lyrics.data.source.song.SongRepository
import com.patagonian.lyrics.domain.data.Songs
import org.koin.core.qualifier.named
import com.patagonian.lyrics.data.local.mapper.SongMapper as LocalSongMapper
import org.koin.dsl.module
import retrofit2.Retrofit

private enum class Environment {
    LOCAL, REMOTE
}

val localRepositoryModule = module {
    single { AppDatabase.create(get()) }
    factory { get<AppDatabase>().songDao() }
    single(named(Environment.LOCAL)) { LocalSongMapper.INSTANCE }

    factory<Songs>(named(Environment.LOCAL)) {
        LocalSongRepository(get(named(Environment.LOCAL)), get())
    }
}

val remoteRepositoryModule = module {
    single { Api.create() }
    factory { get<Retrofit>().create(SongService::class.java) }
    single(named(Environment.REMOTE)) { RemoteSongMapper.INSTANCE }

    factory<Songs>(named(Environment.REMOTE)) {
        RemoteSongRepository(get(named(Environment.REMOTE)), get())
    }
}

val repositoryModule = module {
    factory<Songs> {
        SongRepository(get(named(Environment.LOCAL)), get(named(Environment.REMOTE)))
    }
}

val repositoryModules = arrayOf(localRepositoryModule, remoteRepositoryModule, repositoryModule)