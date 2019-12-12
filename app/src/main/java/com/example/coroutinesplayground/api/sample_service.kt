package com.example.coroutinesplayground.api

import com.example.coroutinesplayground.models.MovieDetailsResponse
import com.example.coroutinesplayground.models.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("/3/movie/popular")
    suspend fun movieList(@Query("api_key") apiKey: String): MoviesResponse

    @GET("/3/movie/{id}")
    suspend fun movieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsResponse
}