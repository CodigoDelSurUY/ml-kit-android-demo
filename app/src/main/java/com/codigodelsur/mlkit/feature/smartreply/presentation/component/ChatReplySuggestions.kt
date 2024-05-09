package com.codigodelsur.mlkit.feature.smartreply.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codigodelsur.mlkit.core.presentation.theme.MlkTheme

@Composable
fun ChatReplySuggestions(
    modifier: Modifier = Modifier,
    suggestions: List<String>,
    onSuggestionClicked: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(suggestions) {
            ReplySuggestionItem(suggestion = it, onClick = { onSuggestionClicked(it) })
        }
    }
}

@Composable
private fun ReplySuggestionItem(suggestion: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = suggestion)
    }
}

@Preview
@Composable
private fun ChatReplySuggestionsPreview() {
    MlkTheme {
        ChatReplySuggestions(
            modifier = Modifier.fillMaxWidth(),
            suggestions = listOf("OK", "No way!", "Maybe..."),
            onSuggestionClicked = {}
        )
    }
}
