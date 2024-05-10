package com.codigodelsur.mlkit.feature.documentscanner.presentation.model

import android.net.Uri

data class PScannedDocument(
    val pdf: Uri,
    val pages: List<Uri>
)