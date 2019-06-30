package com.zkatemor.movies.util

import com.zkatemor.movies.essence.Movie
import com.zkatemor.movies.presenter.MainPresenter

/**
 * интерфейс возможных состояний при запросе данных (успешно, безуспешно, пустая выдача)
 */
interface LoadData {
    fun onSuccess(movies: ArrayList<Movie>, dataLoadStatus: MainPresenter.DataLoadStatus)
    fun onFailure(dataLoadStatus: MainPresenter.DataLoadStatus)
    fun onEmpty(movie: String)
}