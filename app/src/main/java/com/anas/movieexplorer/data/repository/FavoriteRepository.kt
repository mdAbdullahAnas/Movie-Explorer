package com.anas.movieexplorer.data.repository

import com.anas.movieexplorer.data.local.FavoriteMovieDao
import com.anas.movieexplorer.data.local.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val dao: FavoriteMovieDao
) {

    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>> {
        return dao.getAllFavorites()
    }

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return dao.isFavorite(movieId)
    }

    suspend fun addFavorite(movie: FavoriteMovieEntity) {
        dao.insert(movie)
    }

    suspend fun removeFavorite(movieId: Int) {
        dao.deleteById(movieId)
    }

    suspend fun getFavoriteById(movieId: Int): FavoriteMovieEntity? {
        return dao.getFavoriteById(movieId)
    }
}