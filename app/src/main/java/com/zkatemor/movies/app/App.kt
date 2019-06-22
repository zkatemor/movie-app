package com.zkatemor.movies.app

import android.app.Application
import com.zkatemor.movies.di.AppComponent
import com.zkatemor.movies.di.DaggerAppComponent

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }

    companion object {
        var component: AppComponent? = null
            private set
    }
}