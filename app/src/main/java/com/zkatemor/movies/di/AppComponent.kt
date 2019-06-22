package com.zkatemor.movies.di

import com.zkatemor.movies.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun injectsMainActivity(mainActivity: MainActivity)
}