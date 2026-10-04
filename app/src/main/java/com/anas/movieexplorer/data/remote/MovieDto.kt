package com.anas.movieexplorer.data.remote

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double,
    val vote_count: Int
)
