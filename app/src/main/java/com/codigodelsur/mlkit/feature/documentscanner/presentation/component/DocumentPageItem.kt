package com.codigodelsur.mlkit.feature.documentscanner.presentation.component

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun DocumentPageItem(
    modifier: Modifier,
    page: Uri
) {
    AsyncImage(
        modifier = modifier,
        model = page,
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}