package com.zkatemor.movies.network

import com.google.gson.annotations.SerializedName
import com.zkatemor.movies.model.Movie

interface ApiResponse

data class MoviesResponse(
    @SerializedName("results")
    val movies: List<Movies>
) : ApiResponse

data class Movies(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val name: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("release_date")
    val date: String,
    @SerializedName("poster_path")
    val imageURL: String
)