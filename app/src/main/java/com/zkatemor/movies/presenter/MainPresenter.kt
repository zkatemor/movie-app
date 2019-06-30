package com.zkatemor.movies.presenter

import com.zkatemor.movies.app.App
import com.zkatemor.movies.essence.Movie
import com.zkatemor.movies.model.MainModel
import com.zkatemor.movies.util.LoadData
import com.zkatemor.movies.view.MainView

class MainPresenter : LoadData {

    private var model: MainModel? = null
    private var main_view: MainView? = null

    init {
        model = MainModel(this)
        App.component!!.injectsMainPresenter(this)
    }

    fun create(mainView: MainView) {
        main_view = mainView
    }

    override fun onFailure(dataLoadStatus: DataLoadStatus) {
        hideProgress(dataLoadStatus)

        when (dataLoadStatus) {
            DataLoadStatus.Loading, DataLoadStatus.Searching -> {
                if (!main_view!!.hasContent())
                    main_view!!.showErrorLayout()
                else main_view!!.showConnectivityError()
            }
            DataLoadStatus.Refreshing -> main_view!!.showConnectivityError()
        }
    }

    override fun onSuccess(movies: ArrayList<Movie>, dataLoadStatus: DataLoadStatus) {
        hideProgress(dataLoadStatus)
        main_view!!.showMovies(movies)
    }

    override fun onEmpty(movie: String) {
        main_view!!.showFindEmpty(movie)
    }

    fun addMovies() {
        getMainMovies(DataLoadStatus.Loading)
    }

    fun updateMovies(boolean: Boolean, movie: String) {
        if (!boolean)
            getMainMovies(DataLoadStatus.Refreshing)
        else getResultMovies(movie)
    }

    fun searchMovies(movie: String) {
        getResultMovies(movie)
    }

    private fun getMainMovies(dataLoadStatus: DataLoadStatus) {
        showProgress(dataLoadStatus)
        model!!.addAllMovies(dataLoadStatus)
    }

    private fun getResultMovies(movie: String) {
        showProgress(DataLoadStatus.Searching)
        model!!.searchMovies(movie)
    }

    private fun showProgress(dataLoadStatus: DataLoadStatus) {
        when (dataLoadStatus) {
            DataLoadStatus.Loading -> main_view!!.showMainProgressBar()
            DataLoadStatus.Searching -> main_view!!.showProgressBar()
            else -> return
        }
    }

    private fun hideProgress(dataLoadStatus: DataLoadStatus) {
        when (dataLoadStatus) {
            DataLoadStatus.Loading -> main_view!!.hideMainProgressBar()
            DataLoadStatus.Searching -> main_view!!.hideProgressBar()
            DataLoadStatus.Refreshing -> main_view!!.showSwipeRefresh()
        }
    }

    enum class DataLoadStatus {
        Loading,
        Refreshing,
        Searching
    }
}