package com.zkatemor.movies.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("movie?api_key=6ccd72a2a8fc239b13f209408fc31c33&language=ru"
        +"&query=%D1%87%D0%B5%D0%BB%D0%BE%D0%B2%D0%B5%D0%BA%20%D0%BF%D0%B0%D1%83%D0%BA&page=1&include_adult=false&region=ru")
    fun searchMovies(@Query("query") query: String): Call<MoviesResponse>
}