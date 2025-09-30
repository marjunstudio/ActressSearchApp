package com.actresssearch.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import com.actresssearch.presentation.search.ActressSearchRoute
import com.actresssearch.ui.theme.ActressSearchAppTheme

/**
 * Root composable that hosts the search experience.
 */
@Composable
fun ActressSearchApp() {
    ActressSearchAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ActressSearchRoute()
        }
    }
}
