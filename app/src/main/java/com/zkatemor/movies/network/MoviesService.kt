package com.zkatemor.movies.network

import retrofit2.Call
import retrofit2.http.GET

interface MoviesService {
    @GET("movie?api_key=6ccd72a2a8fc239b13f209408fc31c33"
        +"&language=ru&region=ru&sort_by=popularity.desc&include_adult=false&include_video=false&page=1")
    fun getMovies(): Call<MoviesResponse>
}