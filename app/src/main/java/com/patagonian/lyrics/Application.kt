package com.patagonian.lyrics

import com.patagonian.lyrics.di.presenterModule
import com.patagonian.lyrics.di.repositoryModules
import com.patagonian.lyrics.di.useCaseModule
import com.patagonian.lyrics.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(presenterModule, viewModelModule, useCaseModule, *repositoryModules)
        }
    }
}