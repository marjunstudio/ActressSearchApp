package com.actresssearch.domain.repository

import com.actresssearch.domain.model.Actress
import com.actresssearch.domain.model.ActressSearchParams
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction to coordinate data sources for actress information.
 */
interface ActressRepository {
    suspend fun searchActresses(params: ActressSearchParams): Result<List<Actress>>

    suspend fun getActressById(id: String): Result<Actress>

    fun observeFavoriteActresses(): Flow<List<Actress>>

    suspend fun addFavorite(actress: Actress): Result<Unit>

    suspend fun removeFavorite(actressId: String): Result<Unit>

    suspend fun isFavorite(actressId: String): Result<Boolean>
}
