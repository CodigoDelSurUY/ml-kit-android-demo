package com.example.mlkit.core.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mlkit.R

sealed class PMLKitFeature(
    @DrawableRes open val imageRes: Int,
    @StringRes open val titleRes: Int,
    @StringRes open val descriptionRes: Int,
) {
    object DocumentScanner : PMLKitFeature(
        imageRes = R.drawable.card_document_scanner,
        titleRes = R.string.feature_document_scanner_title,
        descriptionRes = R.string.feature_document_scanner_description
    )

    object TextRecognition : PMLKitFeature(
        imageRes = R.drawable.card_text_recognition,
        titleRes = R.string.feature_text_recognition_title,
        descriptionRes = R.string.feature_text_recognition_description
    )

    companion object {
        val all = listOf(
            DocumentScanner,
            TextRecognition
        )
    }
}