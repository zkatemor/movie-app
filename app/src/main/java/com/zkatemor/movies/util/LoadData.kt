package com.zkatemor.movies.util

import com.zkatemor.movies.essence.Movie
import com.zkatemor.movies.presenter.MainPresenter

interface LoadData {
    fun onSuccess(movies: ArrayList<Movie>, dataLoadStatus: MainPresenter.DataLoadStatus)
    fun onFailure(dataLoadStatus: MainPresenter.DataLoadStatus)
    fun onEmpty(movie: String)
}