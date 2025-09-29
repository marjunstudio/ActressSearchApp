package com.actresssearch.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_actresses")
data class FavoriteActressEntity(
    @PrimaryKey val actressId: String,
    val addedAt: Long = System.currentTimeMillis()
)
