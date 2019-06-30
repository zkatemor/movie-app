package com.zkatemor.movies.model

import com.zkatemor.movies.presenter.MainPresenter

interface Model {
    fun addAllMovies(dataLoadStatus: MainPresenter.DataLoadStatus)
    fun searchMovies(movie: String, dataLoadStatus: MainPresenter.DataLoadStatus)
}