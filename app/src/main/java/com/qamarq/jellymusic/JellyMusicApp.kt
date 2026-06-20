package com.qamarq.jellymusic

import android.app.Application
import com.qamarq.jellymusic.core.AppContainer

class JellyMusicApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }

    override fun onTerminate() {
        runCatching { container.release() }
        super.onTerminate()
    }
}
