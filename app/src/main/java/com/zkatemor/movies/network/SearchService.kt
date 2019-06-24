package com.zkatemor.movies.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("movie?api_key=6ccd72a2a8fc239b13f209408fc31c33&language=ru-RU&query=%D0%90%D0%BC%D1%84%D0%B8%D0%B1%D0%B8%D1%8F")
    fun searchMovies(@Query("query") query: String): Call<MoviesResponse>
}