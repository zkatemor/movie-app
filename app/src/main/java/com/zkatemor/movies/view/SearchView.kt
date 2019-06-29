package com.zkatemor.movies.view

interface SearchView {
    fun showFindEmpty(movie: String)
    fun showProgressBar()
    fun hideProgressBar()
}