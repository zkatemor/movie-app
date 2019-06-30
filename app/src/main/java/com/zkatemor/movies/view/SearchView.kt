package com.zkatemor.movies.view

/**
 * интерфейс экрана для поискового запроса
 */
interface SearchView {
    fun showFindEmpty(movie: String)
    fun showProgressBar()
    fun hideProgressBar()
}