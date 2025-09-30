package com.actresssearch.presentation.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
import kotlin.ranges.ClosedFloatingPointRange

class ActressSearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ActressSearchUiState())
    val uiState: StateFlow<ActressSearchUiState> = _uiState.asStateFlow()

    fun onSearchModeChange(mode: SearchMode) {
        _uiState.update { current ->
            if (current.searchMode == mode) current else current.copy(searchMode = mode, snackbarMessage = null)
        }
    }

    fun onKeywordChange(value: String) {
        _uiState.update { it.copy(keyword = value, snackbarMessage = null) }
    }

    fun onBustRangeChange(range: ClosedFloatingPointRange<Float>) {
        updateRange(range) { updated ->
            _uiState.update { current ->
                if (current.bustRange == updated) current else current.copy(bustRange = updated, snackbarMessage = null)
            }
        }
    }

    fun onWaistRangeChange(range: ClosedFloatingPointRange<Float>) {
        updateRange(range) { updated ->
            _uiState.update { current ->
                if (current.waistRange == updated) current else current.copy(waistRange = updated, snackbarMessage = null)
            }
        }
    }

    fun onHipRangeChange(range: ClosedFloatingPointRange<Float>) {
        updateRange(range) { updated ->
            _uiState.update { current ->
                if (current.hipRange == updated) current else current.copy(hipRange = updated, snackbarMessage = null)
            }
        }
    }

    fun onHeightRangeChange(range: ClosedFloatingPointRange<Float>) {
        updateRange(range) { updated ->
            _uiState.update { current ->
                if (current.heightRange == updated) current else current.copy(heightRange = updated, snackbarMessage = null)
            }
        }
    }

    fun onSearch() {
        val state = _uiState.value
        val snackbar = when (state.searchMode) {
            SearchMode.KEYWORD -> validateKeyword(state.keyword)
            SearchMode.BODY -> validateMeasurements(state)
        }

        if (snackbar != null) {
            _uiState.update { it.copy(snackbarMessage = snackbar) }
            return
        }

        val description = when (state.searchMode) {
            SearchMode.KEYWORD -> "キーワード: ${state.keyword}".trim()
            SearchMode.BODY -> {
                val conditions = listOfNotNull(
                    formatRange("バスト", state.bustRange, MeasurementDefaults.BUST),
                    formatRange("ウエスト", state.waistRange, MeasurementDefaults.WAIST),
                    formatRange("ヒップ", state.hipRange, MeasurementDefaults.HIP),
                    formatRange("身長", state.heightRange, MeasurementDefaults.HEIGHT)
                )
                if (conditions.isEmpty()) "" else "サイズ指定: ${conditions.joinToString(separator = ", ")}"
            }
        }

        _uiState.update {
            it.copy(
                snackbarMessage = "検索条件を送信しました",
                lastSearchDescription = description.ifBlank { null },
                isSearching = true
            )
        }
    }

    fun onClearConditions() {
        _uiState.update {
            it.copy(
                keyword = "",
                bustRange = MeasurementDefaults.BUST,
                waistRange = MeasurementDefaults.WAIST,
                hipRange = MeasurementDefaults.HIP,
                heightRange = MeasurementDefaults.HEIGHT,
                snackbarMessage = null,
                lastSearchDescription = null
            )
        }
    }

    fun onSnackbarDisplayed() {
        _uiState.update { it.copy(snackbarMessage = null, isSearching = false) }
    }

    private fun updateRange(
        range: ClosedFloatingPointRange<Float>,
        block: (IntRange) -> Unit
    ) {
        val updated = range.start.roundToInt()..range.endInclusive.roundToInt()
        block(updated)
    }

    private fun validateKeyword(keyword: String): String? =
        if (keyword.isBlank()) "キーワードを入力してください" else null

    private fun validateMeasurements(state: ActressSearchUiState): String? {
        val hasInput = listOf(
            state.bustRange != MeasurementDefaults.BUST,
            state.waistRange != MeasurementDefaults.WAIST,
            state.hipRange != MeasurementDefaults.HIP,
            state.heightRange != MeasurementDefaults.HEIGHT
        ).any { it }
        if (!hasInput) {
            return "いずれかのサイズを調整してください"
        }
        return null
    }

    private fun formatRange(label: String, range: IntRange, defaultRange: IntRange): String? {
        if (range == defaultRange) return null
        return "$label ${range.first}〜${range.last}cm"
    }
}
