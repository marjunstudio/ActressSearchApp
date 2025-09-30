package com.actresssearch.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.actresssearch.ui.theme.ActressSearchAppTheme
import kotlin.math.max

@Composable
fun ActressSearchRoute(
    viewModel: ActressSearchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ActressSearchScreen(
        uiState = uiState,
        onModeChange = viewModel::onSearchModeChange,
        onKeywordChange = viewModel::onKeywordChange,
        onBustRangeChange = viewModel::onBustRangeChange,
        onWaistRangeChange = viewModel::onWaistRangeChange,
        onHipRangeChange = viewModel::onHipRangeChange,
        onHeightRangeChange = viewModel::onHeightRangeChange,
        onSearch = viewModel::onSearch,
        onClearConditions = viewModel::onClearConditions,
        onSnackbarDisplayed = viewModel::onSnackbarDisplayed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActressSearchScreen(
    uiState: ActressSearchUiState,
    onModeChange: (SearchMode) -> Unit,
    onKeywordChange: (String) -> Unit,
    onBustRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onWaistRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHipRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHeightRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onSearch: () -> Unit,
    onClearConditions: () -> Unit,
    onSnackbarDisplayed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onSnackbarDisplayed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "女優検索") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SearchModeSelector(
                    selectedMode = uiState.searchMode,
                    onModeChange = onModeChange
                )

                when (uiState.searchMode) {
                    SearchMode.KEYWORD -> KeywordSearchSection(
                        keyword = uiState.keyword,
                        onKeywordChange = onKeywordChange,
                        onSearch = onSearch
                    )

                    SearchMode.BODY -> BodySearchSection(
                        bustRange = uiState.bustRange,
                        waistRange = uiState.waistRange,
                        hipRange = uiState.hipRange,
                        heightRange = uiState.heightRange,
                        onBustRangeChange = onBustRangeChange,
                        onWaistRangeChange = onWaistRangeChange,
                        onHipRangeChange = onHipRangeChange,
                        onHeightRangeChange = onHeightRangeChange
                    )
                }

                SearchActions(
                    isSearching = uiState.isSearching,
                    onSearch = onSearch,
                    onClearConditions = onClearConditions
                )

                AnimatedVisibility(
                    visible = uiState.lastSearchDescription != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    uiState.lastSearchDescription?.let { description ->
                        SearchSummaryCard(description = description)
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchModeSelector(
    selectedMode: SearchMode,
    onModeChange: (SearchMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "検索方法を選択",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SearchMode.values().forEach { mode ->
                FilterChip(
                    selected = selectedMode == mode,
                    onClick = { onModeChange(mode) },
                    label = { Text(text = mode.displayName) }
                )
            }
        }
        val description = when (selectedMode) {
            SearchMode.KEYWORD -> "名前やフリーワードで素早く検索できます"
            SearchMode.BODY -> "バスト・ウエストなどのサイズ条件で絞り込みます"
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun KeywordSearchSection(
    keyword: String,
    onKeywordChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "キーワード", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = keyword,
            onValueChange = onKeywordChange,
            singleLine = true,
            label = { Text("女優名や関連ワード") },
            placeholder = { Text("例: 田中 / ショートカット") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() })
        )
    }
}

@Composable
private fun BodySearchSection(
    bustRange: IntRange,
    waistRange: IntRange,
    hipRange: IntRange,
    heightRange: IntRange,
    onBustRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onWaistRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHipRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onHeightRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(text = "サイズで検索", style = MaterialTheme.typography.titleMedium)
        MeasurementSlider(
            label = "バスト",
            range = bustRange,
            defaultRange = MeasurementDefaults.BUST,
            valueRange = MeasurementDefaults.BUST,
            onRangeChange = onBustRangeChange
        )
        MeasurementSlider(
            label = "ウエスト",
            range = waistRange,
            defaultRange = MeasurementDefaults.WAIST,
            valueRange = MeasurementDefaults.WAIST,
            onRangeChange = onWaistRangeChange
        )
        MeasurementSlider(
            label = "ヒップ",
            range = hipRange,
            defaultRange = MeasurementDefaults.HIP,
            valueRange = MeasurementDefaults.HIP,
            onRangeChange = onHipRangeChange
        )
        MeasurementSlider(
            label = "身長",
            range = heightRange,
            defaultRange = MeasurementDefaults.HEIGHT,
            valueRange = MeasurementDefaults.HEIGHT,
            onRangeChange = onHeightRangeChange
        )
        Text(
            text = "左右のハンドルで下限と上限を調整できます。初期位置のままの場合、その項目は条件に含まれません。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeasurementSlider(
    label: String,
    range: IntRange,
    defaultRange: IntRange,
    valueRange: IntRange,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.titleSmall)
            val display = if (range == defaultRange) {
                "未指定"
            } else {
                "${range.first}〜${range.last}cm"
            }
            Text(
                text = display,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        RangeSlider(
            value = range.first.toFloat()..range.last.toFloat(),
            onValueChange = onRangeChange,
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            steps = max(0, valueRange.last - valueRange.first - 1)
        )
    }
}

@Composable
private fun SearchActions(
    isSearching: Boolean,
    onSearch: () -> Unit,
    onClearConditions: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSearch,
            enabled = !isSearching,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(text = if (isSearching) "検索中..." else "この条件で検索")
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClearConditions
        ) {
            Text(text = "条件をリセット")
        }
    }
}

@Composable
private fun SearchSummaryCard(description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "直近の検索条件",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActressSearchScreenKeywordPreview() {
    ActressSearchAppTheme {
        ActressSearchScreen(
            uiState = ActressSearchUiState(keyword = "田中"),
            onModeChange = {},
            onKeywordChange = {},
            onBustRangeChange = {},
            onWaistRangeChange = {},
            onHipRangeChange = {},
            onHeightRangeChange = {},
            onSearch = {},
            onClearConditions = {},
            onSnackbarDisplayed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ActressSearchScreenBodyPreview() {
    ActressSearchAppTheme {
        ActressSearchScreen(
            uiState = ActressSearchUiState(
                searchMode = SearchMode.BODY,
                bustRange = 88..95,
                waistRange = 58..65,
                hipRange = 88..93,
                heightRange = 160..168,
                lastSearchDescription = "サイズ指定: バスト 88〜95cm, ウエスト 58〜65cm"
            ),
            onModeChange = {},
            onKeywordChange = {},
            onBustRangeChange = {},
            onWaistRangeChange = {},
            onHipRangeChange = {},
            onHeightRangeChange = {},
            onSearch = {},
            onClearConditions = {},
            onSnackbarDisplayed = {}
        )
    }
}
