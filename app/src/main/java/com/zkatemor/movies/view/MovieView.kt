package com.zkatemor.movies.view

import com.zkatemor.movies.essence.Movie

interface MovieView {
    fun showMovies(data: ArrayList<Movie>)
    fun showErrorLayout()
    fun showMainProgressBar()
    fun hideMainProgressBar()
    fun showSwipeRefresh()
}