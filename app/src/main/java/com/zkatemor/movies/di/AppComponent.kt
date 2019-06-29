package com.zkatemor.movies.di

import com.zkatemor.movies.activity.MainActivity
import com.zkatemor.movies.model.MainModel
import com.zkatemor.movies.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectsMainActivity(mainActivity: MainActivity)
    fun injectsMainModel(mainModel: MainModel)
    fun injectsMainPresenter(mainPresenter: MainPresenter)
}