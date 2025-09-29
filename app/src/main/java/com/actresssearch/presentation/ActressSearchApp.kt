package com.actresssearch.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.actresssearch.ui.theme.ActressSearchAppTheme

/**
 * Top-level composable hosting app-wide theming and scaffolding.
 */
@Composable
fun ActressSearchApp() {
    ActressSearchAppTheme {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Actress Search App")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActressSearchAppPreview() {
    ActressSearchApp()
}
