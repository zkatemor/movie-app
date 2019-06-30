package com.zkatemor.movies.view

import com.zkatemor.movies.essence.Movie

/**
 * интерфейс главного экрана
 */
interface MovieView {
    fun showMovies(data: ArrayList<Movie>)
    fun showErrorLayout()
    fun showMainProgressBar()
    fun hideMainProgressBar()
    fun showSwipeRefresh()
}