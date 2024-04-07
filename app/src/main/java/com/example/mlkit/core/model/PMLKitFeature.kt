package com.example.mlkit.core.model

import androidx.annotation.StringRes
import com.example.mlkit.R

sealed class PMLKitFeature(
    @StringRes open val label: Int
) {
    class DocumentScanner : PMLKitFeature(label = R.string.document_scanner_label)
}