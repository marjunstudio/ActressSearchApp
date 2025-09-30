package com.actresssearch.presentation.search

enum class SearchMode(val displayName: String) {
    KEYWORD("キーワード検索"),
    BODY("サイズ検索")
}

object MeasurementDefaults {
    val BUST = 70..110
    val WAIST = 50..80
    val HIP = 80..110
    val HEIGHT = 145..180
}

data class ActressSearchUiState(
    val searchMode: SearchMode = SearchMode.KEYWORD,
    val keyword: String = "",
    val bustRange: IntRange = MeasurementDefaults.BUST,
    val waistRange: IntRange = MeasurementDefaults.WAIST,
    val hipRange: IntRange = MeasurementDefaults.HIP,
    val heightRange: IntRange = MeasurementDefaults.HEIGHT,
    val isSearching: Boolean = false,
    val snackbarMessage: String? = null,
    val lastSearchDescription: String? = null
)
