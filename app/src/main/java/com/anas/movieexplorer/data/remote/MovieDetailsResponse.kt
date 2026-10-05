package com.anas.movieexplorer.data.remote

data class MovieDetailsResponse(
    val id: Int,
    val title: String,
    val overview: String?,
    val release_date: String?,
    val runtime: Int?,
    val vote_average: Double?,
    val backdrop_path: String?,
    val poster_path: String?,
    val genres: List<Genre>?
)

data class Genre(
    val id: Int,
    val name: String
)