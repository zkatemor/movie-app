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

    /**
     * обработка ошибок
     */
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

    /**
     * обработка успешного взятия данных
     */
    override fun onSuccess(movies: ArrayList<Movie>, dataLoadStatus: DataLoadStatus) {
        hideProgress(dataLoadStatus)
        main_view!!.showMovies(movies)
    }

    /**
     * обработка при отсутствии данных по поисковому запросу
     */
    override fun onEmpty(movie: String) {
        main_view!!.showFindEmpty(movie)
    }

    /**
     * загрузка данных на главный экран
     */
    fun addMovies() {
        getMainMovies(DataLoadStatus.Loading)
    }

    /**
     * обновление данных
     */
    fun updateMovies(isSearch: Boolean, movie: String) {
        if (!isSearch)
            getMainMovies(DataLoadStatus.Refreshing)
        else getResultMovies(movie, DataLoadStatus.Refreshing)
    }

    /**
     * загрузка данных по поисковому запросу
     */
    fun searchMovies(movie: String) {
        getResultMovies(movie, DataLoadStatus.Searching)
    }


    private fun getMainMovies(dataLoadStatus: DataLoadStatus) {
        showProgress(dataLoadStatus)
        model!!.addAllMovies(dataLoadStatus)
    }

    private fun getResultMovies(movie: String, dataLoadStatus: DataLoadStatus) {
        showProgress(DataLoadStatus.Searching)
        model!!.searchMovies(movie, dataLoadStatus)
    }

    /**
     * отобразить состояние прогрузки данных
     */
    private fun showProgress(dataLoadStatus: DataLoadStatus) {
        main_view!!.showProgressBar()
    }

    /**
     * скрыть состояние прогрузки данных
     */
    private fun hideProgress(dataLoadStatus: DataLoadStatus) {
        when (dataLoadStatus) {
            DataLoadStatus.Loading, DataLoadStatus.Searching -> main_view!!.hideProgressBar()
            DataLoadStatus.Refreshing -> main_view!!.showSwipeRefresh()
        }
    }

    /**
     * перечисление возможных состояний (загрузка, обновление, поиск)
     */
    enum class DataLoadStatus {
        Loading,
        Refreshing,
        Searching
    }
}