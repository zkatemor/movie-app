package com.zkatemor.movies.network

const val BASE_URL = "https://api.themoviedb.org/3/discover/"
const val BASE_SEARCH_URL = "https://api.themoviedb.org/3/search/"

interface ResponseCallback<R> {
    fun onSuccess(apiResponse: R)
    fun onFailure(errorMessage: String)
}