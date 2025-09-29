package com.actresssearch.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActressApiResponse(
    val request: RequestInfo,
    val result: ResultInfo
)

@JsonClass(generateAdapter = true)
data class RequestInfo(
    val parameters: Map<String, String>
)

@JsonClass(generateAdapter = true)
data class ResultInfo(
    val status: Int,
    @Json(name = "result_count") val resultCount: Int,
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "first_position") val firstPosition: Int,
    val actress: List<ActressDto>
)

@JsonClass(generateAdapter = true)
data class ActressDto(
    val id: String,
    val name: String,
    val ruby: String,
    val bust: String?,
    val cup: String?,
    val waist: String?,
    val hip: String?,
    val height: String?,
    val birthday: String?,
    @Json(name = "blood_type") val bloodType: String?,
    val hobby: String?,
    val prefectures: String?,
    @Json(name = "imageURL") val imageUrl: ImageUrls,
    @Json(name = "listURL") val listUrl: ListUrls
)

@JsonClass(generateAdapter = true)
data class ImageUrls(
    val small: String,
    val large: String
)

@JsonClass(generateAdapter = true)
data class ListUrls(
    val digital: String,
    val monthly: String,
    val mono: String
)
