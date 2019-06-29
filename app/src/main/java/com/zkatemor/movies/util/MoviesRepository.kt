package com.zkatemor.movies.util

import com.zkatemor.movies.network.MoviesResponse
import com.zkatemor.movies.network.MoviesService
import com.zkatemor.movies.network.ResponseCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val moviesApi: MoviesService) {
    fun getMovies(responseCallback: ResponseCallback<MoviesResponse>) {
        //retrofit async
        moviesApi.getMovies()
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