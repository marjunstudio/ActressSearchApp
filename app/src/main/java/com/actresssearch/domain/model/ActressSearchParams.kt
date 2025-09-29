package com.actresssearch.domain.model

data class ActressSearchParams(
    val initial: String? = null,
    val keyword: String? = null,
    val gteBust: Int? = null,
    val lteBust: Int? = null,
    val gteWaist: Int? = null,
    val lteWaist: Int? = null,
    val gteHip: Int? = null,
    val lteHip: Int? = null,
    val gteHeight: Int? = null,
    val lteHeight: Int? = null,
    val gteBirthday: String? = null,
    val lteBirthday: String? = null,
    val hits: Int = 20,
    val offset: Int = 1,
    val sort: String = "name"
)
