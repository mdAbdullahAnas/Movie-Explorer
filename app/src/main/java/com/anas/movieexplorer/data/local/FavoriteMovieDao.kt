package com.anas.movieexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteById(movieId: Int)

    @Query("SELECT * FROM favorite_movies ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM favorite_movies
            WHERE id = :movieId
        )
    """)
    fun isFavorite(movieId: Int): Flow<Boolean>

    @Query("""
        SELECT * FROM favorite_movies
        WHERE id = :movieId
        LIMIT 1
    """)
    suspend fun getFavoriteById(movieId: Int): FavoriteMovieEntity?
}