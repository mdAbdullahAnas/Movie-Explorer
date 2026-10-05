package com.anas.movieexplorer.data.repository

import com.anas.movieexplorer.data.remote.MovieApiService

class MovieRepository(
    private val apiService: MovieApiService
) {

    suspend fun getPopularMovies() =
        apiService.getPopularMovies()

    suspend fun getNowPlayingMovies() =
        apiService.getNowPlayingMovies()

    suspend fun getTopRatedMovies() =
        apiService.getTopRatedMovies()

    // SEARCH MOVIES
    suspend fun searchMovies(query: String) =
        apiService.searchMovies(query = query)
}