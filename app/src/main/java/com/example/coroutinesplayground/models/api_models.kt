package com.example.coroutinesplayground.models


import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("overview") val overview: String = "",
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    @SerializedName("video") val video: Boolean = false,
    @SerializedName("title") val title: String = "",
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("backdrop_path") val backdropPath: String = "",
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("vote_count") val voteCount: Int = 0
)

data class MoviesResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("total_pages") val totalPages: Int = 0,
    @SerializedName("results") val results: List<Movie> = emptyList(),
    @SerializedName("total_results") val totalResults: Int = 0
)

data class MovieDetailsResponse(
    @SerializedName("original_language") val originalLanguage: String = "",
    @SerializedName("imdb_id") val imdbId: String = "",
    @SerializedName("video") val video: Boolean = false,
    @SerializedName("title") val title: String = "",
    @SerializedName("backdrop_path") val backdropPath: String = "",
    @SerializedName("revenue") val revenue: Int = 0,
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("budget") val budget: Int = 0,
    @SerializedName("overview") val overview: String = "",
    @SerializedName("original_title") val originalTitle: String = "",
    @SerializedName("runtime") val runtime: Int = 0,
    @SerializedName("poster_path") val posterPath: String = "",
    @SerializedName("release_date") val releaseDate: String = "",
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("tagline") val tagline: String = "",
    @SerializedName("adult") val adult: Boolean = false,
    @SerializedName("status") val status: String = ""
)