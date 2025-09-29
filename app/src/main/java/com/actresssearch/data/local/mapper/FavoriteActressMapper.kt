package com.actresssearch.data.local.mapper

import com.actresssearch.data.local.entity.FavoriteActressEntity
import com.actresssearch.domain.model.Actress

fun FavoriteActressEntity.toDomain(actress: Actress): Actress = actress.copy(isFavorite = true)

fun Actress.toFavoriteEntity(): FavoriteActressEntity = FavoriteActressEntity(actressId = id)
