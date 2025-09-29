package com.actresssearch.data.remote.dto

import com.actresssearch.domain.model.Actress
import com.actresssearch.domain.model.ImageUrls as DomainImageUrls
import com.actresssearch.domain.model.ListUrls as DomainListUrls

fun ActressDto.toDomain(isFavorite: Boolean = false): Actress = Actress(
    id = id,
    name = name,
    ruby = ruby,
    bust = bust,
    cup = cup,
    waist = waist,
    hip = hip,
    height = height,
    birthday = birthday,
    bloodType = bloodType,
    hobby = hobby,
    prefectures = prefectures,
    imageUrl = DomainImageUrls(
        small = imageUrl.small,
        large = imageUrl.large
    ),
    listUrl = DomainListUrls(
        digital = listUrl.digital,
        monthly = listUrl.monthly,
        mono = listUrl.mono
    ),
    isFavorite = isFavorite
)

fun List<ActressDto>.toDomain(): List<Actress> = map { it.toDomain() }
