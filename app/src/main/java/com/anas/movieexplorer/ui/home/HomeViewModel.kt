package com.anas.movieexplorer.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.movieexplorer.data.remote.MovieDto
import com.anas.movieexplorer.data.remote.RetrofitClient
import com.anas.movieexplorer.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository(RetrofitClient.api)

    private val _movies = MutableStateFlow<List<MovieDto>>(emptyList())
    val movies: StateFlow<List<MovieDto>> = _movies

    private val _nowPlayingMovies =
        MutableStateFlow<List<MovieDto>>(emptyList())

    val nowPlayingMovies: StateFlow<List<MovieDto>> =
        _nowPlayingMovies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadPopularMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = repository.getPopularMovies()

                Log.d(
                    "MOVIE_TEST",
                    "Popular movies: ${response.results.size}"
                )

                _movies.value = response.results

            } catch (e: Exception) {
                Log.e(
                    "MOVIE_TEST",
                    "Popular API ERROR",
                    e
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNowPlayingMovies() {
        viewModelScope.launch {
            try {

                val response = repository.getNowPlayingMovies()

                Log.d(
                    "MOVIE_TEST",
                    "Now Playing movies: ${response.results.size}"
                )

                _nowPlayingMovies.value = response.results

            } catch (e: Exception) {

                Log.e(
                    "MOVIE_TEST",
                    "Now Playing API ERROR",
                    e
                )
            }
        }
    }
}