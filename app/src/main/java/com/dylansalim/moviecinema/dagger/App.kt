package com.dylansalim.moviecinema.dagger

import android.app.Application
import com.dylansalim.moviecinema.dagger.component.AppComponent
import com.dylansalim.moviecinema.dagger.component.DaggerAppComponent
import com.dylansalim.moviecinema.dagger.module.networkModule.NetworkModule

open class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .networkModule(NetworkModule(this))
            .build()
    }
}