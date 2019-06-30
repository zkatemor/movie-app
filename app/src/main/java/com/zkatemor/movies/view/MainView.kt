package com.zkatemor.movies.view

/**
 * интерфейс приложения
 */
interface MainView: SearchView, MovieView {
    fun hasContent(): Boolean
    fun showConnectivityError()
}