package com.actresssearch.domain.model

data class Actress(
    val id: String,
    val name: String,
    val ruby: String,
    val bust: String?,
    val cup: String?,
    val waist: String?,
    val hip: String?,
    val height: String?,
    val birthday: String?,
    val bloodType: String?,
    val hobby: String?,
    val prefectures: String?,
    val imageUrl: ImageUrls,
    val listUrl: ListUrls,
    val isFavorite: Boolean = false
)

data class ImageUrls(
    val small: String,
    val large: String
)

data class ListUrls(
    val digital: String,
    val monthly: String,
    val mono: String
)
