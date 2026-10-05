package com.anas.movieexplorer.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.anas.movieexplorer.data.local.FavoriteMovieEntity
import com.anas.movieexplorer.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return repository.isFavorite(movieId)
    }

    fun getFavorites(): Flow<List<FavoriteMovieEntity>> {
        return repository.getAllFavorites()
    }

    fun toggleFavorite(movie: FavoriteMovieEntity) {
        viewModelScope.launch {

            val existingMovie =
                repository.getFavoriteById(movie.id)

            if (existingMovie == null) {
                repository.addFavorite(movie)
            } else {
                repository.removeFavorite(movie.id)
            }
        }
    }

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeFavorite(movieId)
        }
    }
}

class FavoriteViewModelFactory(
    private val repository: FavoriteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                FavoriteViewModel::class.java
            )
        ) {
            return FavoriteViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}