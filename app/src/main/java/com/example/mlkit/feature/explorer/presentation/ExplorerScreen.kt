package com.example.mlkit.feature.explorer.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mlkit.app.ui.theme.MlkTheme
import com.example.mlkit.app.ui.theme.Typography
import com.example.mlkit.core.presentation.model.PMLKitFeature

@Composable
fun ExplorerRoute(
    modifier: Modifier = Modifier,
    viewModel: ExplorerViewModel = hiltViewModel(),
    onFeatureClick: (PMLKitFeature) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
    Card(
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = feature.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.30f)
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(id = feature.titleRes),
                style = Typography.titleLarge
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                text = stringResource(id = feature.descriptionRes),
                style = Typography.bodyMedium
            )
        }
    }
}


@Preview
@Composable
private fun FeatureItemPreview() {
    MlkTheme {
        FeatureItem(feature = PMLKitFeature.DocumentScanner, onClick = {})
    }
}

@Preview
@Composable
private fun ExplorerScreenPreview() {
    MlkTheme {
        ExplorerScreen(
            features = PMLKitFeature.all,
            onFeatureClick = {}
        )
    }
}