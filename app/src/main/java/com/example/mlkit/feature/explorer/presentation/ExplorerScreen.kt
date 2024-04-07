package com.example.mlkit.feature.explorer.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.app.ui.theme.MLKitTheme
import com.example.mlkit.core.model.PMLKitFeature

@Composable
fun ExplorerRoute(
    modifier: Modifier = Modifier,
    explorerViewModel: ExplorerViewModel = hiltViewModel(),
    onFeatureClick: (PMLKitFeature) -> Unit
) {
    val state by explorerViewModel.state.collectAsStateWithLifecycle()
    ExplorerScreen(
        modifier = modifier,
        features = state.features,
        onFeatureClick = onFeatureClick
    )
}

@Composable
private fun ExplorerScreen(
    modifier: Modifier = Modifier,
    features: List<PMLKitFeature>,
    onFeatureClick: (PMLKitFeature) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(features) { feature ->
            FeatureItem(
                feature = feature,
                onClick = { onFeatureClick(feature) }
            )
        }
    }
}

@Composable
private fun FeatureItem(feature: PMLKitFeature, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            )
    ) {
        Text(text = stringResource(id = feature.label))
    }
}


@Preview
@Composable
private fun ExplorerScreenPreview() {
    MLKitTheme {
        ExplorerScreen(features = listOf(PMLKitFeature.DocumentScanner()), onFeatureClick = {})
    }
}