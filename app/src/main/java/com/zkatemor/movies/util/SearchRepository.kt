package com.zkatemor.movies.util

import com.zkatemor.movies.network.MoviesResponse
import com.zkatemor.movies.network.ResponseCallback
import com.zkatemor.movies.network.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchApi: SearchService) {
    fun searchMovies(responseCallback: ResponseCallback<MoviesResponse>, query: String) {
        //retrofit async
        searchApi.searchMovies(query)
            .enqueue(object : Callback<MoviesResponse> {

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    responseCallback.onFailure("Getting movies error")
                }

                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    val moviesResponse = response.body()

                    if (moviesResponse != null) {
                        responseCallback.onSuccess(moviesResponse)
                    } else {
                        responseCallback.onFailure("Getting movies error")
                    }
                }
            })
    }
}