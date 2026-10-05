package com.anas.movieexplorer.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.movieexplorer.data.remote.MovieDto
import com.anas.movieexplorer.data.remote.RetrofitClient
import com.anas.movieexplorer.data.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository =
        MovieRepository(RetrofitClient.api)

    private val _searchResults =
        MutableStateFlow<List<MovieDto>>(emptyList())

    val searchResults: StateFlow<List<MovieDto>> =
        _searchResults.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    private var searchJob: Job? = null

    fun searchMovies(query: String) {

        searchJob?.cancel()

        val cleanQuery = query.trim()

        if (cleanQuery.isEmpty()) {
            _searchResults.value = emptyList()
            _error.value = null
            _isLoading.value = false
            return
        }

        searchJob = viewModelScope.launch {

            delay(400)

            try {

                _isLoading.value = true
                _error.value = null

                Log.d(
                    "SEARCH_API",
                    "Searching for: $cleanQuery"
                )

                val response =
                    repository.searchMovies(cleanQuery)

                Log.d(
                    "SEARCH_API",
                    "Results: ${response.results.size}"
                )

                _searchResults.value =
                    response.results

            } catch (e: Exception) {

                Log.e(
                    "SEARCH_ERROR",
                    "Movie search failed",
                    e
                )

                _searchResults.value = emptyList()

                _error.value =
                    e.message ?: "Something went wrong"

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {

        searchJob?.cancel()

        _searchResults.value = emptyList()
        _error.value = null
        _isLoading.value = false
    }
}