package com.zkatemor.movies.view

interface MainView: SearchView, MovieView {
    fun hasContent(): Boolean
}