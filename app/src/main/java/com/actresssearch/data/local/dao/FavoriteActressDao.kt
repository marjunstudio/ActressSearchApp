package com.actresssearch.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.actresssearch.data.local.entity.FavoriteActressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteActressDao {
    @Query("SELECT * FROM favorite_actresses ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteActressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteActressEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteActressEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_actresses WHERE actressId = :actressId)")
    suspend fun isFavorite(actressId: String): Boolean
}
